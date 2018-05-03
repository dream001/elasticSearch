package com.yonyou.zookeeper;


/**
 * 角色分布：
 * 
    Client：产生议题者
    Proposer ：提议者
    Acceptor：决策者
    Learner：最终决策学习者，也就是执行者。
 * 
 * 
 * 准备阶段：
 * 1）proposer向acceptor提出一个协议，这里的协议就是期望的“一致性内容”
 * 2）acceptor承诺只接收最大协议号的协议（包括prepare和accept），并拒绝比当前协议号N小的协议，
 * 回复proposer之前接收的所有协议值。如果当前协议号N比之前都小，那么回复拒绝。
 * 
 * 
 * 同意阶段：
 * 1） Accept Request 发起“accept”请求：proposer收到acceptor反馈的足够的承诺后，
 * 给协议设最大值，如果没回复，随便设置一个值。发送"accept"请求给选定值的acceptors.
 * 2）Accepted:acceptor接受协议（该acceptor之前没有承诺过大于该协议号的协议），
 * 并通知给proposer和learner.
 * 
 * 
 * 
 * 原则：
 * 安全原则---保证不能做错的事
 * 1. 只能有一个值被批准，不能出现第二个值把第一个覆盖的情况
 * 2. 每个节点只能学习到已经被批准的值，不能学习没有被批准的值
 * 
 * 存活原则---只要有多数服务器存活并且彼此间可以通信最终都要做到的事
 * 1. 最终会批准某个被提议的值
 * 2. 一个值被批准了，其他服务器最终会学习到这个值
 * 
 * 
 * 
 * 
 * 每个人都可以提议，只接受标志大与当前的接受的提议
 * 提议没有半数，重复第一个过程
 * 
 * 
 * @ClassName: PoxesTest
 * @Description: TODO(类简要描述，必须以句号为结束)
 * @author caozq  
 * @date 2018年4月17日
 */
public class PoxesTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
