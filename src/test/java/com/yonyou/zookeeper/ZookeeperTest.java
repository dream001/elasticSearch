package com.yonyou.zookeeper;


/**
 * 应用：
 * 
 * 配置管理：程序总是需要配置的，如果程序分散部署在多台机器上，要逐个改变配置就变得困难。
 * 现在把这些配置全部放到zookeeper上去，保存在 Zookeeper 的某个目录节点中，
 * 然后所有相关应用程序对这个目录节点进行监听，一旦配置信息发生变化，
 * 每个应用程序就会收到 Zookeeper 的通知，然后从 Zookeeper 获取新的配置信息应用到系统中就好
 * 
 * 
 * 名字服务：类似一个域名到IP的转换，每一个目录都有唯一一个Path 能够相互探索发现
 * 
 * 
 * 文件系统：每个子目录项如 NameService 都被称作为znode，和文件系统一样，
 * 我们能够自由的增加、删除znode，在一个znode下增加、删除子znode，唯一的不同在于znode是
 * 可以存储数据的
 * 
 * 
 * 分布式锁：一致性文件系统，锁的问题变得容易。锁服务可以分为两类，一个是保持独占，
 * 另一个是控制时序。对于第一类，我们将zookeeper上的一个znode看作是一把锁，
 * 通过createznode的方式来实现。所有客户端都去创建 /distribute_lock 节点，
 * 最终成功创建的那个客户端也即拥有了这把锁。用完删除掉自己创建的distribute_lock 节点就释放出锁。
 * 对于第二类， /distribute_lock 已经预先存在，所有客户端在它下面创建临时顺序编号目录节点，
 * 和选master一样，编号最小的获得锁，用完删除，依次方便
 * 
 * 
 * 集群管理：是否有机器退出和加入、选举master。对于第一点，所有机器约定在父目录GroupMembers
 * 下创建临时目录节点，然后监听父目录节点的子节点变化消息。一旦有机器挂掉，
 * 该机器与 zookeeper的连接断开，其所创建的临时目录节点被删除，
 * 所有其他机器都收到通知：某个兄弟目录被删除，于是，所有人都知道：它上船了。
 * 新机器加入也是类似，所有机器收到通知：新兄弟目录加入，highcount又有了，对于第二点，
 * 我们稍微改变一下，所有机器创建临时顺序编号目录节点，每次选取编号最小的机器作为master就好。
 * 
 * 队列管理：1）同步队列，当一个队列的成员都聚齐时，这个队列才可用，否则一直等待所有成员到达，
 * 在约定目录下创建临时目录节点，监听节点数目是否是我们要求的数目
 * 2）队列按照 FIFO 方式进行入队和出队操作，和分布式锁服务中的控制时序场景基本原理一致，
 * 入列有编号，出列按编号
 * 
 * 
 * 特点：
 * 最终一致性：为客户端展示同一视图，这是zookeeper最重要的功能
 * 可靠性：如果消息被到一台服务器接受，那么它将被所有的服务器接受
 * 顺序性：所有Server，同一消息发布顺序一致
 * 原子性：更新只能成功或者失败，没有中间状态。
 * 实时性：Zookeeper不能保证两个客户端能同时得到刚更新的数据，如果需要最新数据，应该在读数据之前调用sync()接口
 * 等待无关（wait-free）：慢的或者失效的client不干预快速的client的请求
 * 
 * 
 * zookeeper 在实际中的使用：
 * HDFS中的HA方案 
 * YARN的HA方案
 * HBase：必须依赖Zookeeper，保存了Regionserver的心跳信息，和其他的一些关键信息。
 * Flume：负载均衡，单点故障
 * 
 * 
 * 流程：
 *  1 每个Server在内存中存储了一份数据； 
    2 Zookeeper启动时，将从实例中选举一个leader（Paxos协议）； 
    3 Leader负责处理数据更新等操作（Zab协议）； 
    4 一个更新操作成功，当且仅当大多数Server在内存中成功修改数据
 * 
 * leader: 负责投票的发起和决议，更新系统状态
 * leaner:
 *      Follower:接受客户请求并向客户端返回结果，在选主过程中参与投票
 *      Observer:接受客户连接，将写请求转发给Leader节点。不参与投票，
 *      只同步Leader的状态。目的是为了扩展系统，提高读的速度
 *      
 * Client:  请求发起方
 * 
 * 
 * 
 * 要点：
 * 1)Zookeeper Server数目一般为奇数，Leader选举算法采用了Paxos协议；
 * Paxos核心思想：当多数Server写成功，则任务数据写
 * 
 * 2)Observer节点增加原因： Zookeeper需保证高可用和强一致性.
 * 当集群节点数目逐渐增大为了支持更多的客户端，需要增加更多Server，
 * 然而Server增多，投票阶段延迟增大，影响性能。为了权衡伸缩性和高吞吐率，引入Observer：
 * Observer不参与投票； Observers接受客户端的连接，并将写请求转发给leader节点； 
 * 加入更多Observer节点，提高伸缩性，同时不影响吞吐率。提供了写任意的原则
 * 
 * 
 * 
 * 
 * Zookeeper工作原理：
 * Zookeeper 的核心是原子广播，这个机制保证了各个Server之间的同步。实现这个机制的协议
 * 叫做Zab协议。Zab协议有两种模式，它们分别是恢复模式（选主）和广播模式（同步）。
 * 当服务启动或者在领导者崩溃后，Zab就进入了恢复模式，当领导者被选举出来，
 * 且大多数Server完成了和 leader的状态同步以后，恢复模式就结束了。
 * 状态同步保证了leader和Server具有相同的系统状态。 
 * 为了保证事务的顺序一致性，zookeeper采用了递增的事务id号（zxid）来标识事务。
 * 所有的提议（proposal）都在被提出的时候加上了zxid。实现中zxid是一个64位的数字，
 * 它高32位是epoch用来标识leader关系是否改变，每次一个leader被选出来，
 * 它都会有一个新的epoch，标识当前属于那个leader的统治时期。低32位用于递增计数。
 * 
 * 
 * Zookeeper 下 Server工作状态：
 * 每个Server在工作过程中有三种状态： 
 * LOOKING：当前Server不知道leader是谁，正在搜寻。
 * LEADING：当前Server即为选举出来的leader。
 * FOLLOWING：leader已经选举出来，当前Server与之同步
 * 
 * 选主策略： 
 * base paxos   选举进程想 Server 发起询问，记录(id zxid)  生成投票记录表中合并计算出半数选择server
 * fast paxos(默认)  建议自己是主 
 * 
 * 
 * 同步流程：
 * 1. Leader等待server连接； 
 * 2 .Follower连接leader，将最大的zxid发送给leader； 
 * 3 .Leader根据follower的zxid确定同步点；
 * 4 .完成同步后通知follower 已经成为uptodate状态；
 * 5 .Follower收到uptodate消息后，又可以重新接受client的请求进行服务了。
 * 
 * 
 * 保证事物的一致性：高32位  zxid用于标志是否改变   低32位用于递增计数
 * 
 * 
 * Zookeeper工作流程-Leader
 * 1 .恢复数据；
 * 2 .维持与Learner的心跳，接收Learner请求并判断Learner的请求消息类型； 
 * 3 .Learner的消息类型主要有PING消息、REQUEST消息、ACK消息、REVALIDATE消息，根据不同的消息类型，进行不同的处理。
 * PING 消息是指Learner的心跳信息；
 * REQUEST消息是Follower发送的提议信息，包括写请求及同步请求；
 * ACK消息是 Follower的对提议的回复，超过半数的Follower通过，则commit该提议；
 * REVALIDATE消息是用来延长SESSION有效时间。
 * 
 * 
 * Zookeeper工作流程-Follower
 * 1.向Leader发送请求（PING消息、REQUEST消息、ACK消息、REVALIDATE消息）；
 * 2.接收Leader消息并进行处理；
 * 3.接收Client的请求，如果为写请求，发送给Leader进行投票；
 * 4.返回Client结果
 * 
 * 
 * Follower的消息循环处理如下几种来自Leader的消息：
 * 1 .PING消息： 心跳消息； 
 * 2 .PROPOSAL消息：Leader发起的提案，要求Follower投票；
 * 3 .COMMIT消息：服务器端最新一次提案的信息；
 * 4 .UPTODATE消息：表明同步完成；
 * 5 .REVALIDATE消息：根据Leader的REVALIDATE结果，关闭待revalidate的session还是允许其接受消息；
 * 
 * 
 * @ClassName: ZookeeperTest
 * @Description: TODO(类简要描述，必须以句号为结束)
 * @author caozq
 * @date 2018年4月16日
 */
public class ZookeeperTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
