package com.yonyou.kudu;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: ImpalaKudu
 * @Description: Impala JDBC并发测试
 * @date 2018年3月15日
 */
public class ImpalaKudu {

    public static void main(String[] args) throws InterruptedException {
        ImpalaKudu impala = new ImpalaKudu();
        ExecutorService executor = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 200; i++) {
            executor.execute(() -> {
                impala.testSelectAll();
            });
        }
    }

    public void testSelectAll() {
        Connection conn = null;
        Statement stamt = null;
        ResultSet rs = null;
        String sqlStr = "select * from t_user1";
        try {
            conn = ImpalaJDBC.getConnection();
            stamt = ImpalaJDBC.getStatement(conn);
            long startTime = System.currentTimeMillis();
            rs = stamt.executeQuery(sqlStr);
            long endTime = System.currentTimeMillis();
//            System.out.printf("execute sql time = %sms\n", (endTime - startTime));
            
            long metaStartTime = System.currentTimeMillis();
            int count = rs.getMetaData().getColumnCount();
            long metaEndTime = System.currentTimeMillis();
//            System.out.printf("get metadata from resultset time = %sms, column count=%d\n", (metaEndTime - metaStartTime), count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ImpalaJDBC.closeResultSet(rs);
            ImpalaJDBC.closeStatement(stamt);
            ImpalaJDBC.closeConnection(conn);
        }
    }

}
