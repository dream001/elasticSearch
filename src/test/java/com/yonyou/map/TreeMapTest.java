package com.yonyou.map;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class TreeMapTest {


    /**
     * 定义逆序排列
     */
    static NavigableMap<Integer, List<Integer>> maps = new TreeMap<>(new Comparator<Integer>() {
        @Override
        public int compare(Integer a, Integer b) {
            return b - a;
        }
    });
    
    
    /**
     * 默认排序方式
     */
    
    static NavigableMap<Integer, String> mapss = new TreeMap<>(new Comparator<Integer>() {

        @Override
        public int compare(Integer o1, Integer o2) {
            // TODO Auto-generated method stub
            return o1-o2;
        }
        
    });


    /**
     * 遍历方式
     * @Title: print
     * @Description: TODO(方法简要描述，必须以句号为结束)
     * @author: caozq
     * @since: (开始使用的版本)
     */
    private static void print() {
        
        for (Map.Entry<Integer, List<Integer>> entry : maps.entrySet()) {
            System.out.println(entry.getKey().toString() + entry.getValue());
        }
        
        
        for(Map.Entry<Integer, String> mao:mapss.entrySet()){
            
        }
        
        
    }


    public static void main(String[] args) {
        

    }
    
    
    

}
