package util;

import java.net.InetAddress;

public class ClusterUtils {
    private static String hostName;
    static {
        try {
            InetAddress netAddress = InetAddress.getLocalHost();
            hostName = netAddress.getHostAddress();
        } catch (Exception e) {
            hostName = "0.0.0.1";
        }
    }

    public static String getHostName() {
        return hostName;
    }

    /**
     * 分布式时每个节点生成的主键前缀和主机IP相关
     */
    public static String getHostPK() {
        String pk = hostName.replaceAll("\\.", "");
        return pk.substring(pk.length()-4);
    }
    
    public static String getHostPK4() {
        String pk = hostName.replaceAll("\\.", "");
        return pk.substring(pk.length()-4,pk.length());
    }
    
    public static String getHostSuf() {
        String pk = hostName.replaceAll("\\.", "");
        return pk.substring(pk.length()-1,pk.length());
    }
    
    public static void main(String[] args){
        System.out.println(getHostSuf());
    }
}
