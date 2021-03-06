//package util;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//
//import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
//import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse.AnalyzeToken;
//import org.elasticsearch.action.get.GetResponse;
//import org.elasticsearch.action.search.SearchRequestBuilder;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.action.search.SearchType;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.client.IndicesAdminClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.text.Text;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.MatchQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.SearchHits;
//import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
//import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
//import org.elasticsearch.search.sort.SortOrder;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
//
///**
// * 
// * @ClassName: ESUtil
// * @Description: TODO(类简要描述，必须以句号为结束)
// * @author caozq
// * @date 2018年4月20日
// */
//public class ESUtil {
//	private static byte[] LOCK = new byte[0];
//	private static Client client = null;
//	
//	
//	/**
//	 * 读取配置文件
//	 * @param propFile
//	 * @return
//	 */
//	public static Properties readProperties(String propFile) {
//        Properties props = new Properties();
//        try (InputStream is = Thread.currentThread().getContextClassLoader()
//                .getResourceAsStream(propFile)) {
//            props.load(is);
//        } catch (IOException e){
//        	//logger.error(e);
//        }
//        return props;
//    }
//
//	/**
//	 * 获取客户端
//	 * 
//	 * @return
//	 */
//	public static Client getClient() {
//		synchronized (LOCK) {
//			if (client == null) {
//				Properties props = readProperties("config.properties");
//				// put("client.transport.sniff", true) 外网ip访问最好不用这个
//				Settings settings = Settings.builder().put("cluster.name", props.getProperty("es.cluster.name")).build();
//				try {
//					String[] nodeHosts = props.getProperty("es.cluster.hosts").split(",");
//					PreBuiltTransportClient preClient = new PreBuiltTransportClient(settings);
//					for (String nodeHost : nodeHosts){
//						preClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(nodeHost), Integer.valueOf(props.getProperty("es.cluster.port"))));
//					}
//					client = preClient;
//				} catch (UnknownHostException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return client;
//	}
//	
//	/**
//	 * 关闭
//	 */
//	public static void closeClient(){
//		if (client != null){
//			client.close();
//		}
//	}
//	
//	public static String searchDocById(String index, String type, String id) {
//	    GetResponse getResponse = getClient()
//	                                .prepareGet()   // 准备进行get操作，此时还有真正地执行get操作。（与直接get的区别）
//	                                .setIndex(index)  // 要查询的
//	                                .setType(type)
//	                                .setId(id)
//	                                .get();
//	    return getResponse.getSourceAsString();
//	}
//	
//	
//	/**
//	 * @param index
//	 * @param types
//	 * @param field
//	 * @param fieldValue
//	 * @param fromIndex
//	 * @param size
//	 * @param orderByField
//	 * @return
//	 */
//	public static Map<String, Object> termQueryByPage(String index, String[] types,String[] fields, String[] fieldValues,int fromIndex, int size, String orderByField, String[] excludesFields, boolean must) {
//		SearchRequestBuilder builder = getClient().prepareSearch(index);
//		
//		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//		int len = fieldValues.length;
//		for (int i = 0; i < len; i ++){
//			if ("all".equals(fieldValues[i])){
//				continue;
//			}
//			//must 必须是fields 和 fieldValues 一一对应,第0个肯定是must
//			if(i==0 || must){
//				QueryBuilder termBuilder = QueryBuilders.termQuery(fields[i], fieldValues[i]);
//				boolQueryBuilder.must(termBuilder);
//			}else{
//				// mustnot and i > 0  其他排序情况
//				QueryBuilder termBuilder = QueryBuilders.termQuery(fields[1], fieldValues[i]);
//				boolQueryBuilder.mustNot(termBuilder);
//			}
//		}
//		
//		SearchResponse searchResponse = builder
//		        .setTypes(types)
//		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)//// 设置查询类型 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询 2.SearchType.SCAN 扫描查询,无序
//		        .setQuery(boolQueryBuilder)
//		        .setFrom(fromIndex).setSize(size).setExplain(true).addSort(orderByField, SortOrder.DESC)// from 从0开始  是否排序
//		        .get();
//		SearchHits hits = searchResponse.getHits();
//		long total = hits.getTotalHits();
//		Map<String, Object> map = new HashMap<String, Object>();
//		SearchHit[] hits2 = hits.getHits();
//		map.put("count", total);
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		for (SearchHit searchHit : hits2) {
//			Map<String, Object> source = searchHit.getSource();
//			source.put("_id", searchHit.getId());
//			if (excludesFields != null){
//				for (String removeField : excludesFields){
//					source.remove(removeField);
//				}
//			}
//			list.add(source);
//		}
//		map.put("dataList", list);
//		return map;
//	}
//	
//	/**
//	 * @param index
//	 * @param types
//	 * @param field
//	 * @param fieldValue
//	 * @param fromIndex
//	 * @param size
//	 * @param orderByField
//	 * @return
//	 */
//	public static Map<String, Object> termQueryRange(String index, String[] types,String field, String fieldValue, String rangeField, String rangeFrom, String rangeTo, String[] excludesFields) {
//		SearchRequestBuilder builder = getClient().prepareSearch(index);
//		
//		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//		QueryBuilder termBuilder = QueryBuilders.termQuery(field, fieldValue);
//		boolQueryBuilder.must(termBuilder);
//		
//		QueryBuilder rangeBuilder = QueryBuilders.rangeQuery(rangeField).from(rangeFrom).to(rangeTo);
//		boolQueryBuilder.must(rangeBuilder);
//		
//		SearchResponse searchResponse = builder
//		        .setTypes(types)
//		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)//// 设置查询类型 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询 2.SearchType.SCAN 扫描查询,无序
//		        .setQuery(boolQueryBuilder)
//		        .setFrom(0).setSize(200).setExplain(true)// from 从0开始  是否排序
//		        .get();
//		SearchHits hits = searchResponse.getHits();
//		long total = hits.getTotalHits();
//		Map<String, Object> map = new HashMap<String, Object>();
//		SearchHit[] hits2 = hits.getHits();
//		map.put("count", total);
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		for (SearchHit searchHit : hits2) {
//			Map<String, Object> source = searchHit.getSource();
//			source.put("_id", searchHit.getId());
//			if (excludesFields != null){
//				for (String removeField : excludesFields){
//					source.remove(removeField);
//				}
//			}
//			list.add(source);
//		}
//		map.put("dataList", list);
//		return map;
//	}
//	
//	
//	/**
//	 * 模糊查询
//	 * @param index
//	 * @param types
//	 * @param field
//	 * @param fieldValue
//	 * @return
//	 */
//	public static Map<String, Object> wildcardQuery(String index, String[] types,String field, String fieldValue) {
//		SearchRequestBuilder builder = getClient().prepareSearch(index);
//		
//		
//		//QueryBuilder queryBuilder = QueryBuilders.prefixQuery(field, fieldValue);
//		QueryBuilder queryBuilder = QueryBuilders.wildcardQuery(field, "*"+fieldValue+"*");
//		
//		SearchResponse searchResponse = builder
//		        .setTypes(types)
//		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)//// 设置查询类型 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询 2.SearchType.SCAN 扫描查询,无序
//		        .setQuery(queryBuilder)
//		        .setFrom(0).setSize(200).setExplain(true)// from 从0开始  是否排序
//		        .get();
//		SearchHits hits = searchResponse.getHits();
//		long total = hits.getTotalHits();
//		Map<String, Object> map = new HashMap<String, Object>();
//		SearchHit[] hits2 = hits.getHits();
//		map.put("count", total);
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		for (SearchHit searchHit : hits2) {
//			Map<String, Object> source = searchHit.getSource();
//			source.put("_id", searchHit.getId());
//			list.add(source);
//		}
//		map.put("dataList", list);
//		return map;
//	}
//	
//	/**
//	 * 全文检索
//	 * @param key
//	 * @param index
//	 * @param type
//	 * @param fields
//	 * @param from
//	 * @param size
//	 * @param highlight
//	 * @return
//	 */
//	public static Map<String, Object> search(String key, String[] index, String[] type, String[] fields,int from, int size, boolean highlight, String[] excludesFields, String orderByField) {
//		SearchRequestBuilder builder = getClient().prepareSearch(index);
//		if (highlight){
//			//设置高亮显示
//			HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
//			highlightBuilder.preTags("<span style=\"color:red\">");
//			highlightBuilder.postTags("</span>");
//			builder.highlighter(highlightBuilder);
//		}
//		
//		key = key.replaceAll("\\s{1,}", "\t").trim();
//		String[] keys = key.split("\t");
//		
//		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//		//多个关键字之间是and的关系
//		for (String splitKey : keys){
//			BoolQueryBuilder shouldQueryBuilder = QueryBuilders.boolQuery();
//			for (String field : fields){
//				MatchQueryBuilder matchQb = QueryBuilders.matchQuery(field, splitKey);
//				matchQb.analyzer("ik_smart");
//				shouldQueryBuilder.should(matchQb);//分词
//			}
//			shouldQueryBuilder.minimumShouldMatch(1);
//			boolQueryBuilder.must(shouldQueryBuilder);
//		}
//		
//		SearchResponse searchResponse = builder
//		        .setTypes(type)
//		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)//// 设置查询类型 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询 2.SearchType.SCAN 扫描查询,无序
//		        .setQuery(boolQueryBuilder)
//		        .setFrom(from).setSize(size).setExplain(true).get();// from 从0开始  是否排序
//
//		SearchHits hits = searchResponse.getHits();
//		long total = hits.getTotalHits();
//		Map<String, Object> map = new HashMap<String, Object>();
//		SearchHit[] hits2 = hits.getHits();
//		map.put("count", total);
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		boolean first = true;
//		float highScore = 5;
//		float standScore = 2.5f;
//		for (SearchHit searchHit : hits2) {
//			if (first){
//				highScore = searchHit.getScore();
//				standScore = (float)(Math.round(highScore*100))/200;
//				first = false;
//			}else{
//				if (searchHit.getScore() < standScore){
//					break;
//				}
//			}
//			
//			Map<String, Object> source = searchHit.getSource();
//			//排序 但是排序的值为空
//			if (!Toolkit.isEmpty(orderByField) && (source.get(orderByField) == null || Toolkit.isEmpty(source.get(orderByField).toString()))){
//				continue;
//			}
//			if (highlight){
//				Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
//				for (String field : fields){
//					HighlightField highlightField = highlightFields.get(field);
//					if (highlightField != null) {
//						Text[] fragments = highlightField.fragments();
//						String name = "";
//						for (Text text : fragments) {
//							name += text; 
//						}
//						source.put(field, name);
//					}
//				}
//			}
//			source.put("_match_score", searchHit.getScore());
//			source.put("_id", searchHit.getId());
//			if (excludesFields != null){
//				for (String removeField : excludesFields){
//					source.remove(removeField);
//				}
//			}
//			list.add(source);
//		}
//		List<Map<String, Object>> orderLst = new ArrayList<Map<String, Object>>();
//		if (!Toolkit.isEmpty(orderByField)){
//			List<String> words = getAnalyzerTokens(index[0], key);
//			List<Map<String, Object>> allMatchLst = new ArrayList<Map<String, Object>>();
//			List<Map<String, Object>> otherLst = new ArrayList<Map<String, Object>>();
//			for (Map<String, Object> innerMap : list){
//				boolean allMatch = false;
//				for (String field : fields){
//					if ((containsAllWords(innerMap.get(field), words))){
//						allMatch = true;
//						break;
//					}
//				}
//				if (allMatch){
//					allMatchLst.add(innerMap);
//				}else{
//					otherLst.add(innerMap);
//				}
//			
//			}
//			
//			// 全部匹配的时间倒序
//			Collections.sort(allMatchLst, (Map<String, Object> m1, Map<String, Object> m2)->{
//				return m2.get(orderByField).toString().compareTo(m1.get(orderByField).toString());
//			});
//			orderLst.addAll(allMatchLst);
//			
//			Collections.sort(otherLst, (Map<String, Object> m1, Map<String, Object> m2)->{
//				// 如果都包含短语,那么得分相同
//				float score1 = Float.valueOf(m1.get("_match_score").toString());
//				float score2 = Float.valueOf(m2.get("_match_score").toString());
//				if (score1 == score2){
//					return m2.get(orderByField).toString().compareTo(m1.get(orderByField).toString());
//				}else if(score2 < score1){
//					return -1;
//				}else{
//					return 1;
//				}
//			});
//			orderLst.addAll(otherLst);
//		}else{
//			orderLst = list;
//		}
//		
////		for (Map<String, Object> innerMap : orderLst){
////			System.out.println(innerMap.get("issue_time") + "---" + innerMap.get("title").toString());
////		}
//		
//		map.put("dataList", orderLst);
//		return map;
//	}
//	
//	private static boolean containsAllWords(Object obj, List<String> words){
//		if (obj == null){
//			return false;
//		}
//		for (String word : words){
//			if (obj.toString().indexOf(word) < 0){
//				return false;
//			}
//		}
//		return true;
//	}
//	
//	/**
//	 * 分词
//	 * @param key
//	 * @return
//	 */
//	public static List<String> getAnalyzerTokens(String index, String key) {
//		IndicesAdminClient indicesAdminClient = getClient().admin().indices();
//		AnalyzeRequestBuilder request = indicesAdminClient.prepareAnalyze(index, key);
//		request.setTokenizer("ik_smart");
//		// Analyzer（分析器）、Tokenizer（分词器）
//		List<AnalyzeToken> analyzeTokens = request.execute().actionGet().getTokens();
//		List<String> results = new ArrayList<String>();
//		for (AnalyzeToken token : analyzeTokens) {
//			results.add(token.getTerm());
//		}
//		return results;
//	}
//
//
//	public static void main(String[] args) throws Exception{
//		 try{
////			 System.out.println("o7pcf0blunyxhIO1w5Ym9fdDK6c4".length());
////			 Map<String, Object> source = new HashMap<String, Object>();
////			 source.remove("test0000");
////			 
////			 Map<String, Object> result  = ESUtil.search("哈尔滨银行", new String[]{"fradar"}, new String[]{"raw_fin_info", "raw_fin_bids"}, new String[]{"ent_name", "title"}, 0, 30, false, new String[]{"content","content_imgs"}, "issue_time");
////			 System.out.println(JsonUtil.toJSON(result));
//			 //Map<String, Object> result  = ESUtil.termQueryByPage("fradar", new String[]{"raw_fin_bids"}, "clazz,ent_name".split(","), ("001,"+"银行,保险,证券,信托,基金,期货,租赁,资产管理,消费金融").split(","), 0, 20, "issue_time_date", new String[]{"content"}, false);
//			 Map<String, Object> result  = ESUtil.wildcardQuery("corp_index", new String[]{"raw_corp"}, "gsmc_kw", "裕东科技");
//			 System.out.println(JsonUtil.toJSON(result));
//			 //JsonUtil.toJSON(searchDocById("fradar", "raw_fin_bids", "AV6StuKsM1qWwYQ20EK5")); 
//			 
//		 }finally{
//			 closeClient();
//		 }
//		//System.out.println(sendGetRequest("http://10.10.4.148:9200/cia_index/_analyze?analyzer=ik_smart&text=%E4%B8%AD%E5%9B%BD"));
//		
////		System.out.println(Toolkit.join(getAnalyzerTokens("中国上海北京"), ","));
//	 }
//}
