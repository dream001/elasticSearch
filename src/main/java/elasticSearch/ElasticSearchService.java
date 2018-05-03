package elasticSearch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse.AnalyzeToken;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import util.Constant;
import util.ProUtils;
import util.Toolkit;


public class ElasticSearchService {

    private final static int MAX = 10000;
    static public TransportClient client;
    private static byte[] LOCK = new byte[0];


    public ElasticSearchService() throws MessageException {
        synchronized (LOCK) {
            if (client == null) {
                Settings settings = Settings.builder().put("cluster.name", ProUtils.getProperty("es.cluster.name"))
                        .build();
                try {
                    String[] nodeHosts = ProUtils.getProperty("es.cluster.hosts").split(",");
                    PreBuiltTransportClient preClient = new PreBuiltTransportClient(settings);
                    for (String nodeHost : nodeHosts) {
                        preClient.addTransportAddress(new TransportAddress(InetAddress.getByName(nodeHost),
                                Integer.valueOf(ProUtils.getProperty("es.cluster.port"))));
                    }
                    client = preClient;
                } catch (UnknownHostException  e) {
                    throw new MessageException("es init failed!", e); 
                }
            }
        }
    }

    /**
     * 创建索引
     * @Title: createIndex
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     * @param indexName
     */
    public void createIndex(String indexName) {
        client.admin().indices().create(new CreateIndexRequest(indexName)).actionGet();
    }

    /**
     * 创建索引
     * @Title: createIndex
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     * @param index
     * @param type
     */
    public void createIndex(String index, String type) {
        client.prepareIndex(index, type).setSource().get();
    }


    /**
     * 删除索引
     * @Title: deleteIndex
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     * @param index
     * @throws MessageException
     */
    public void deleteIndex(String index) throws MessageException {
        if (indexExist(index)) {
            DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(index).execute().actionGet();
            if (!dResponse.isAcknowledged()) {
                throw new MessageException("failed to delete index.");
            }
        } else {
            throw new MessageException("index name not exists");
        }
    }

    /**
     * 判断索引
     * @Title: indexExist
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     * @param index
     * @return
     */
    public boolean indexExist(String index) {
        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(index);
        IndicesExistsResponse inExistsResponse = client.admin().indices().exists(inExistsRequest).actionGet();
        return inExistsResponse.isExists();
    }

    /**
     * 新增数据
     * @Title: bulkInsertData
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     * @param index
     * @param type
     * @param data
     * @return
     */
    public Boolean bulkInsertData(String index, String type, Map<String, Object> data) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        bulkRequest.add(client.prepareIndex(index, type).setSource((Map)data));
        BulkResponse bulkResponse = bulkRequest.get();
        return bulkResponse.hasFailures();
    }
    
    /**
     * 新增数据
     * @Title: bulkInsertData
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     * @param index
     * @param type
     * @param _id
     * @param data
     * @return
     */
    public Boolean bulkInsertData(String index, String type,String _id, Map<String, Object> data) {
        if(null == _id || "".equals(_id)){
            return bulkInsertData(index,type,data);
        }
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        bulkRequest.add(client.prepareIndex(index, type).setId(_id).setSource(data));
        BulkResponse bulkResponse = bulkRequest.get();
        return bulkResponse.hasFailures();
    }

    /**
     * 刪除数据
     * @Title: deleteData
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     * @param index
     * @param type
     * @param _id
     * @return
     */
    public Boolean deleteData(String index, String type, String _id) {
        DeleteResponse response = client.prepareDelete(index, type, _id).get();
        return response.status().getStatus() == Constant.SUCCESS_CODE?true:false;
    }

    /**
     * 修改数据
     * @Title: updateData
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     * @param index
     * @param type
     * @param _id
     * @param map
     * @throws Exception
     */
    public Boolean updateData(String index, String type, String _id, Map<String,Object> map) throws Exception {
        try {
            UpdateRequest updateRequest = new UpdateRequest(index, type, _id).doc(map);
            UpdateResponse response = client.update(updateRequest).get();
            return response.status().getStatus() == Constant.SUCCESS_CODE?true:false;
        } catch (Exception e) {
            throw new MessageException("update data failed.", e);
        }
    }
    
    /**
     * 分词器
     * @Title: getAnalyzerTokens
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     * @param index
     * @param key
     * @return
     */
    public static List<String> getAnalyzerTokens(String index, String key) {
        IndicesAdminClient indicesAdminClient = client.admin().indices();
        AnalyzeRequestBuilder request = indicesAdminClient.prepareAnalyze(index, key);
        request.setTokenizer("ik_smart");
        // Analyzer（分析器）、Tokenizer（分词器）
        List<AnalyzeToken> analyzeTokens = request.execute().actionGet().getTokens();
        List<String> results = new ArrayList<String>();
        for (AnalyzeToken token : analyzeTokens) {
            results.add(token.getTerm());
        }
        return results;
    }
    
    /**
     * 模糊匹配
     * @Title: wildcardQuery
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     * @param index
     * @param types
     * @param field
     * @param fieldValue
     * @return
     */
    public static Map<String, Object> wildcardQuery(String index, String[] types,String field, String fieldValue) {
        SearchRequestBuilder builder = client.prepareSearch(index);
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery(field, "*"+fieldValue+"*");
        SearchResponse searchResponse = builder
                .setTypes(types)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)//// 设置查询类型 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询 2.SearchType.SCAN 扫描查询,无序
                .setQuery(queryBuilder)
                .setFrom(0).setSize(200).setExplain(true)// from 从0开始  是否排序
                .get();
        SearchHits hits = searchResponse.getHits();
        long total = hits.getTotalHits();
        Map<String, Object> map = new HashMap<String, Object>();
        SearchHit[] hits2 = hits.getHits();
        map.put("count", total);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (SearchHit searchHit : hits2) {
            Map<String, Object> source = searchHit.getSourceAsMap();
            source.put("_id", searchHit.getId());
            list.add(source);
        }
        map.put("dataList", list);
        return map;
    }
    
    
    public Map<String, Object> search(String index, String type, ESQueryBuilderConstructor constructor) {
        SearchRequestBuilder builder = client.prepareSearch(index).setTypes(type);
        // 排序
        if (StringUtils.isNotEmpty(constructor.getAsc()))
            builder.addSort(constructor.getAsc(), SortOrder.ASC);
        if (StringUtils.isNotEmpty(constructor.getDesc()))
            builder.addSort(constructor.getDesc(), SortOrder.DESC);
        // 设置查询体
        builder.setQuery(constructor.listBuilders());
        // 返回条目数
        int size = constructor.getSize();
        if (size < 0) {
            size = 0;
        }
        if (size > MAX) {
            size = MAX;
        }
        // 返回条目数
        builder.setSize(size);
        builder.setFrom(constructor.getFrom() < 0 ? 0 : constructor.getFrom());
        SearchResponse searchResponse = builder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).get();
        
        SearchHits hits = searchResponse.getHits();
        long total = hits.getTotalHits();
        Map<String, Object> map = new HashMap<String, Object>();
        SearchHit[] hits2 = hits.getHits();
        map.put("count", total);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (SearchHit searchHit : hits2) {
            Map<String, Object> source = searchHit.getSourceAsMap();
            source.put("_id", searchHit.getId());
            list.add(source);
        }
        map.put("dataList", list);
        return map;
    }
    
    /**
     * 分页查询
     * @Title: termQueryByPage
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     * @param index
     * @param types
     * @param fields
     * @param fieldValues
     * @param fromIndex
     * @param size
     * @param orderByField
     * @param excludesFields
     * @param must
     * @return
     */
    public static Map<String, Object> termQueryByPage(String index, String[] types,String[] fields, String[] fieldValues,int fromIndex, int size, String orderByField, String[] excludesFields, boolean must) {
        SearchRequestBuilder builder = client.prepareSearch(index);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        int len = fieldValues.length;
        for (int i = 0; i < len; i ++){
            if ("all".equals(fieldValues[i])){
                continue;
            }
            //must 必须是fields 和 fieldValues 一一对应,第0个肯定是must
            if(i==0 || must){
                QueryBuilder termBuilder = QueryBuilders.termQuery(fields[i], fieldValues[i]);
                boolQueryBuilder.must(termBuilder);
            }else{
                // mustnot and i > 0  其他排序情况
                QueryBuilder termBuilder = QueryBuilders.termQuery(fields[1], fieldValues[i]);
                boolQueryBuilder.mustNot(termBuilder);
            }
        }
        SearchResponse searchResponse = builder
                .setTypes(types)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)//// 设置查询类型 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询 2.SearchType.SCAN 扫描查询,无序
                .setQuery(boolQueryBuilder)
                .setFrom(fromIndex).setSize(size).setExplain(true).addSort(orderByField, SortOrder.DESC)// from 从0开始  是否排序
                .get();
        SearchHits hits = searchResponse.getHits();
        long total = hits.getTotalHits();
        Map<String, Object> map = new HashMap<String, Object>();
        SearchHit[] hits2 = hits.getHits();
        map.put("count", total);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (SearchHit searchHit : hits2) {
            Map<String, Object> source = searchHit.getSourceAsMap();
            source.put("_id", searchHit.getId());
            if (excludesFields != null){
                for (String removeField : excludesFields){
                    source.remove(removeField);
                }
            }
            list.add(source);
        }
        map.put("dataList", list);
        return map;
    }
    
    /**
     * rang  查询
     * @Title: termQueryRange
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     * @param index
     * @param types
     * @param field
     * @param fieldValue
     * @param rangeField
     * @param rangeFrom
     * @param rangeTo
     * @param excludesFields
     * @return
     */
    public static Map<String, Object> termQueryRange(String index, String[] types,String field, String fieldValue, String rangeField, String rangeFrom, String rangeTo, String[] excludesFields) {
        SearchRequestBuilder builder = client.prepareSearch(index);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        QueryBuilder termBuilder = QueryBuilders.termQuery(field, fieldValue);
        boolQueryBuilder.must(termBuilder);
        QueryBuilder rangeBuilder = QueryBuilders.rangeQuery(rangeField).from(rangeFrom).to(rangeTo);
        boolQueryBuilder.must(rangeBuilder);
        
        SearchResponse searchResponse = builder
                .setTypes(types)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)//// 设置查询类型 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询 2.SearchType.SCAN 扫描查询,无序
                .setQuery(boolQueryBuilder)
                .setFrom(0).setSize(200).setExplain(true)// from 从0开始  是否排序
                .get();
        SearchHits hits = searchResponse.getHits();
        long total = hits.getTotalHits();
        Map<String, Object> map = new HashMap<String, Object>();
        SearchHit[] hits2 = hits.getHits();
        map.put("count", total);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (SearchHit searchHit : hits2) {
            Map<String, Object> source = searchHit.getSourceAsMap();
            source.put("_id", searchHit.getId());
            if (excludesFields != null){
                for (String removeField : excludesFields){
                    source.remove(removeField);
                }
            }
            list.add(source);
        }
        map.put("dataList", list);
        return map;
    }
    
    /**
     * 全文检索
     * @Title: search
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     * @param key
     * @param index
     * @param type
     * @param fields
     * @param from
     * @param size
     * @param highlight
     * @param excludesFields
     * @param orderByField
     * @return
     */
    public static Map<String, Object> search(String key, String[] index, String[] type, String[] fields,int from, int size, boolean highlight, String[] excludesFields, String orderByField) {
        SearchRequestBuilder builder = client.prepareSearch(index);
        if (highlight){
            //设置高亮显示
            HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
            highlightBuilder.preTags("<span style=\"color:red\">");
            highlightBuilder.postTags("</span>");
            builder.highlighter(highlightBuilder);
        }
        
        key = key.replaceAll("\\s{1,}", "\t").trim();
        String[] keys = key.split("\t");
        
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //多个关键字之间是and的关系
        for (String splitKey : keys){
            BoolQueryBuilder shouldQueryBuilder = QueryBuilders.boolQuery();
            for (String field : fields){
                MatchQueryBuilder matchQb = QueryBuilders.matchQuery(field, splitKey);
                matchQb.analyzer("ik_smart");
                shouldQueryBuilder.should(matchQb);//分词
            }
            shouldQueryBuilder.minimumShouldMatch(1);
            boolQueryBuilder.must(shouldQueryBuilder);
        }
        
        SearchResponse searchResponse = builder
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)//// 设置查询类型 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询 2.SearchType.SCAN 扫描查询,无序
                .setQuery(boolQueryBuilder)
                .setFrom(from).setSize(size).setExplain(true).get();// from 从0开始  是否排序

        SearchHits hits = searchResponse.getHits();
        long total = hits.getTotalHits();
        Map<String, Object> map = new HashMap<String, Object>();
        SearchHit[] hits2 = hits.getHits();
        map.put("count", total);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        boolean first = true;
        float highScore = 5;
        float standScore = 2.5f;
        for (SearchHit searchHit : hits2) {
            if (first){
                highScore = searchHit.getScore();
                standScore = (float)(Math.round(highScore*100))/200;
                first = false;
            }else{
                if (searchHit.getScore() < standScore){
                    break;
                }
            }
            
            Map<String, Object> source = searchHit.getSourceAsMap();
            //排序 但是排序的值为空
            if (!Toolkit.isEmpty(orderByField) && (source.get(orderByField) == null || Toolkit.isEmpty(source.get(orderByField).toString()))){
                continue;
            }
            if (highlight){
                Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
                for (String field : fields){
                    HighlightField highlightField = highlightFields.get(field);
                    if (highlightField != null) {
                        Text[] fragments = highlightField.fragments();
                        String name = "";
                        for (Text text : fragments) {
                            name += text; 
                        }
                        source.put(field, name);
                    }
                }
            }
            source.put("_match_score", searchHit.getScore());
            source.put("_id", searchHit.getId());
            if (excludesFields != null){
                for (String removeField : excludesFields){
                    source.remove(removeField);
                }
            }
            list.add(source);
        }
        List<Map<String, Object>> orderLst = new ArrayList<Map<String, Object>>();
        if (!Toolkit.isEmpty(orderByField)){
            List<String> words = getAnalyzerTokens(index[0], key);
            List<Map<String, Object>> allMatchLst = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> otherLst = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> innerMap : list){
                boolean allMatch = false;
                for (String field : fields){
                    if ((containsAllWords(innerMap.get(field), words))){
                        allMatch = true;
                        break;
                    }
                }
                if (allMatch){
                    allMatchLst.add(innerMap);
                }else{
                    otherLst.add(innerMap);
                }
            
            }
            
            // 全部匹配的时间倒序
            Collections.sort(allMatchLst, (Map<String, Object> m1, Map<String, Object> m2)->{
                return m2.get(orderByField).toString().compareTo(m1.get(orderByField).toString());
            });
            orderLst.addAll(allMatchLst);
            
            Collections.sort(otherLst, (Map<String, Object> m1, Map<String, Object> m2)->{
                // 如果都包含短语,那么得分相同
                float score1 = Float.valueOf(m1.get("_match_score").toString());
                float score2 = Float.valueOf(m2.get("_match_score").toString());
                if (score1 == score2){
                    return m2.get(orderByField).toString().compareTo(m1.get(orderByField).toString());
                }else if(score2 < score1){
                    return -1;
                }else{
                    return 1;
                }
            });
            orderLst.addAll(otherLst);
        }else{
            orderLst = list;
        }        
        map.put("dataList", orderLst);
        return map;
    }
    
    
    private static boolean containsAllWords(Object obj, List<String> words){
        if (obj == null){
            return false;
        }
        for (String word : words){
            if (obj.toString().indexOf(word) < 0){
                return false;
            }
        }
        return true;
    }
    
    

    /**
     * 功能描述：关闭链接
     */
    public void close() {
        client.close();
    }
}
