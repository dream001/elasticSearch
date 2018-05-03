//package com.yonyou.tree;
//
//import java.util.Map.Entry;
//
///**
// * 定义：
// * 1.任何一个节点都有颜色，红黑的任何一种
// * 2.根节点是黑色的
// * 3.父子直接不能出现连续的红节点
// * 4.空节点被认为是黑色节点
// * 5.任何一个节点向下遍历其子节点，经过的黑节点的个数是一致的
// * 
// * 
// * 
// * 应用： Map set 中大量使用
// * 
// * 
// * 
// * 
// * http://www.importnew.com/24930.html   treeMap 插入的过程
// * 
// * 
// * 
// * 
// * @ClassName: RBTree
// * @Description: TODO(类简要描述，必须以句号为结束)
// * @author caozq
// * @date 2018年4月23日
// */
//public class RBTree <AnyType extends Comparable<? super AnyType>>{
//
//    private Node<AnyType> root;  
//    private Node<AnyType> nullNode; 
//    private Node<AnyType> header;
//      
//    private static final boolean BLACK=true;  
//    private static final boolean RED  =false; 
//    
//    
//    private static class Node<AnyType>{  
//        Node(AnyType d){  
//            this.data   =d;  
//            this.left   =null;  
//            this.right  =null;  
//            this.parent =null;  
//            this.color  =RBTree.BLACK;  
//        }  
//          
//        Node(AnyType d, Node<AnyType> left, Node<AnyType> right,Node<AnyType> parent,boolean color){  
//            this.data   =d;  
//            this.left   =left;  
//            this.right  =right;  
//            this.parent =parent;  
//            this.color  =color;  
//        }  
//          
//        AnyType         data;  
//        Node<AnyType>     left;  
//        Node<AnyType>     right;  
//        Node<AnyType>     parent;  
//        boolean         color;  
//    }  
//    
//    
//    public RBTree(){          
//        nullNode=new Node<>(null);  
//        nullNode.parent=nullNode.left=nullNode.right=nullNode;  
//        root=nullNode;  
//        header = new Node<>(null, null, root, null, RBTree.BLACK);//header.right->root,  null <- root.parent  
//    }  
//      
//    public void makeEmpty() {  
//        root=nullNode;  
//        header.right=root;  
//    }  
//  
//    public boolean isEmpty() {  
//        return (root==nullNode);  
//    }  
//    
//    
//    
//    
//    
//    /**
//     * /** 
// *  ----------------leftRotate----------------------- 
// *         |k2|                  |k1| 
// *     |k1|    |Z|     --->   |X|    |k2|  
// *   |X|  |Y|               |W|    |Y|  |Z| 
// * |W|   
// * --------------------------------------------------- 
// *         |k2|                  |k1| 
// *     |k1|    |Z|     --->   |X|    |k2|  
// *   |X|  |Y|                      |Y|  |Z| 
// * --------------------------------------------------- 
// * Errors would be arroused if k1 or k2 is null. 
// * */ 
//    private void rotateLeft(Entry<K,V> p) {
//        if (p != null) {
//            Entry<K,V> r = p.right;
//            p.right = r.left;
//            if (r.left != null)
//                r.left.parent = p;
//            r.parent = p.parent;
//            if (p.parent == null)
//                root = r;
//            else if (p.parent.left == p)
//                p.parent.left = r;
//            else
//                p.parent.right = r;
//            r.left = p;
//            p.parent = r;
//        }
//    }
//    
//    
//    /**
//     * /** 
// *  ----------------rightRotate---------------------- 
// *         |k1|                      |k2| 
// *     |Z|     |k2|     --->    |k1|       |Y|  
// *          |X|   |Y|         |Z|  |x|   |W|  
// *              |W|                        
// * --------------------------------------------------- 
// *         |k1|                      |k2| 
// *     |Z|     |k2|     --->    |k1|       |Y|  
// *          |X|   |Y|         |Z|  |x|     
// * ---------------------------------------------------
//     */
//    
//    
//    private void rotateRight(Entry<K,V> p) {
//        if (p != null) {
//            Entry<K,V> l = p.left;
//            p.left = l.right;
//            if (l.right != null) l.right.parent = p;
//            l.parent = p.parent;
//            if (p.parent == null)
//                root = l;
//            else if (p.parent.right == p)
//                p.parent.right = l;
//            else p.parent.left = l;
//            l.right = p;
//            p.parent = l;
//        }
//    }
//    
//    
//}
