package com.yonyou.map;

import java.util.NavigableSet;
import java.util.TreeSet;


/**
 * SortedSet的实现类
 * @ClassName: NavigableSetTest
 * @Description: TODO(类简要描述，必须以句号为结束)
 * @author caozq
 * @date 2018年4月23日
 */
public class NavigableSetTest {


    private static void test() {


        NavigableSet<String> sortedTreeSet = new TreeSet<String>();

        sortedTreeSet.add("aa");
        sortedTreeSet.add("bb");
        sortedTreeSet.add("cc");
        sortedTreeSet.add("dd");
        sortedTreeSet.add("ee");
        sortedTreeSet.add("aa");

        System.out.println(sortedTreeSet.size());// 5个元素：5
        System.out.println(sortedTreeSet.ceiling("cc"));// 大于等于cc的最小值，不存在返回null：cc
        System.out.println(sortedTreeSet.descendingSet());// 返回Set的逆序视图：<span
                                                          // style="font-size:14px;"><span
                                                          // style="color:#000000;background:rgb(255,255,255);"><span
                                                          // style="color:#000000;">[ee, dd, cc, bb,
                                                          // aa]</span></span></span>
        System.out.println(sortedTreeSet.floor("cc"));// 返回小于等于cc的元素的最大值，不存在返回null：cc
        System.out.println(sortedTreeSet.headSet("cc"));// 返回元素小于cc的元素：[aa,bb]
        System.out.println(sortedTreeSet.headSet("cc", true));// 返回元素小于等于cc的元素视图:[aa,bb,cc]

        System.out.println(sortedTreeSet.higher("cc"));// 返回大于给定元素的最小元素:dd
        System.out.println(sortedTreeSet.lower("cc"));// 返回小于cc的最大元素:bb
        System.out.println(sortedTreeSet.pollFirst());// 移除第一个元素:aa
        System.out.println(sortedTreeSet.pollLast());// 移除最后一个元素:ee
        System.out.println(sortedTreeSet.subSet("aa", true, "dd", true));// 返回部分视图，true表示包括当前元素:[bb,cc,dd]
        System.out.println(sortedTreeSet.subSet("bb", "dd"));// 返回部分视图包括前面的，不包括后面的:[bb,cc]
        System.out.println(sortedTreeSet.tailSet("cc"));// 返回元素大于cc的元素视图,包括cc:[cc,dd]
        System.out.println(sortedTreeSet.tailSet("cc", false));// 返回元素大于等于cc的元素视图:[dd]
        System.out.println(sortedTreeSet.iterator());// 返回set上的升序排序的迭代器
        System.out.println(sortedTreeSet.descendingIterator());// 返回set上的降序排序的迭代器

        System.out.println("=====================================");


    }

    public static void main(String[] args) {

        test();
    }

}
