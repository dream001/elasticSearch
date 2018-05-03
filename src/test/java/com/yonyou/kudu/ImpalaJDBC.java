package com.yonyou.kudu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ImpalaJDBC {

    private static String IMPALA_DRIVER = "com.cloudera.impala.jdbc4.Driver";
    private static String CONNECTION_URL = "jdbc:impala://cdh-02:21050/test";

    public static Connection getConnection() throws ClassNotFoundException {
        Connection conn = null;
        long startTime = System.currentTimeMillis();
        try {
            Class.forName(IMPALA_DRIVER);
            conn = DriverManager.getConnection(CONNECTION_URL);
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.printf(Thread.currentThread().getName() + "\tget connection time = %sms\n", (endTime - startTime));
        return conn;
    }

    public static Statement getStatement(Connection conn) throws SQLException {
        long startTime = System.currentTimeMillis();
        if (conn == null) {
            throw new RuntimeException("jdbc connection is null");
        }
        Statement stamt = conn.createStatement();
        long endTime = System.currentTimeMillis();
        // System.out.printf("get statement time = %sms\n", (endTime - startTime));
        return stamt;
    }

    public static void closeResultSet(ResultSet rs) {
        long startTime = System.currentTimeMillis();
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        // System.out.printf("close resultset time = %sms\n", (endTime - startTime));
    }

    public static void closeStatement(Statement stamt) {
        long startTime = System.currentTimeMillis();
        if (stamt != null) {
            try {
                stamt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        // System.out.printf("close statement time = %sms\n", (endTime - startTime));
    }

    public static void closeConnection(Connection conn) {
        long startTime = System.currentTimeMillis();
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
//            conn = null;
        }
        long endTime = System.currentTimeMillis();
         System.out.printf("close connection time = %sms\n", (endTime - startTime));
    }
}
