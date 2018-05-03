package com.yonyou.tree;

import java.util.Map;
import java.util.TreeMap;

/**
 * 排序二叉树:本身就是一个有序的树，每次调整之后也是有序调整
 * 
 * 插入：根节点和非根节点              直接插入，选择最合适的地方插入
 * 删除：
 * 叶子节点：直接删除
 * 没有同时左右孩子:直接候补；
 * 左右节点都有的：两种方式处理：   直接提取，旋转
 * 查找：前序后序中序
 * 
 * 
 * 
 * @ClassName: SortTree
 * @Description: TODO(类简要描述，必须以句号为结束)
 * @author caozq
 * @date 2018年4月24日
 */
public class SortTree {

    public static void main(String[] args) {
        
        Map<Integer,Object> map = new TreeMap<Integer,Object>();
        map.put(1, "11");
        map.put(12, "12");
        map.put(9, "09");
        map.put(10, "10");
        
        
        

    }

}
