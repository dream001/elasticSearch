package elasticSearch;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Toolkit;

public class Test {


    private static void search() {
        ElasticSearchService service = null;
        try {
            service = new ElasticSearchService();
            //Map<String, Object> result  = service.search("哈尔滨银行", new String[]{"fradar"}, new String[]{"raw_fin_info", "raw_fin_bids"}, new String[]{"ent_name", "title"}, 0, 30, false, new String[]{"content","content_imgs"}, "issue_time");
           Map<String, Object> result1  = service.termQueryRange("index_01", new String[]{"student"}, "user", "Tom_01_01", "message","0", "4", null);
            //Map<String, Object> result2  = service.wildcardQuery("index_01", new String[]{"student"}, "user", "01");
            
//            ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();  
//            constructor.must(new ESQueryBuilders().fuzzy("user", "_01"));  
//            constructor.setSize(15);  //查询返回条数，最大 10000  
//            constructor.setFrom(0);  //分页查询条目起始位置， 默认0  
//            //constructor.setAsc("age"); //排序  
//            Map<String, Object> map = service.search("index_01", "student", constructor);
//            
//            for(Map.Entry<String, Object> enter : map.entrySet()){
//                System.out.println(enter.getKey() + enter.getValue());
//            }
        } catch (MessageException e) {
            e.printStackTrace();
        } finally {
            service.close();
        }
    }

    private static void insert() {
        ElasticSearchService service = null;
        try {
            service = new ElasticSearchService();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user", "Tom_01");
            map.put("age", "1");
            map.put("postDate", new Date().toString());
            map.put("message", "success ing");
            service.bulkInsertData("index_01", "student", map);

        } catch (MessageException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            service.close();
        }
    }
    
    
    private static void delete() {
        ElasticSearchService service = null;
        try {
            service = new ElasticSearchService();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user", "Tom_01");
            map.put("postDate", new Date().toString());
            map.put("message", "success ing");
            System.out.println(service.deleteData("index_01", "student", "Qox_-mIBs99kk6eHe5_2")?"success":"false");
        } catch (MessageException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            service.close();
        }
    }
    
    private static void updata() {
        ElasticSearchService service = null;
        try {
            service = new ElasticSearchService();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user", "Tom_01_01");
            map.put("age", "22");
            map.put("postDate",Toolkit.getCurrentTime());
            map.put("message", "heihei");
            System.out.println(service.updateData("index_01", "student", "R4yF-mIBs99kk6eHZp9L",map)?"success":"false");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            service.close();
        }
    }

    public static void main(String[] args) {
        search();
        //updata();

    }


}
