//package com.yonyou.kudu;
//
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
//import org.apache.kudu.Type;
//import org.apache.kudu.client.KuduException;
//
//import nc.jdbc.framework.util.Generator;
//import nc.jdbc.framework.util.SQLHelper;
//import nc.vo.pub.BusinessException;
//import nc.vo.pub.SuperVO;
//
//public class KuduTest {
//    
//    //https://www.cnblogs.com/yangsy0915/p/8059602.html
//    public static void main(String[] args) {
//      //insertSingleTEST();
//        //  insertMultTEST();
//        //  updateMultTEST();
//        //  updateSingleTEST();
//        //  deleteMultTEST();
//        //  deleteSingleTEST();
//        //  renameTEST();
//        //  alterColumnTEST();
//       //selectTest();
//        
//        
//        Lock lock = new ReentrantLock();
//        
//        String[] vos = new String[20000];
//        for(int i = 0;i < 20000;i++){
//            vos[i] = "@@@"+i;
//        }
//        
//        String[] temp = preparePK(vos,false);
//        
//        for(String t:temp){
//            System.out.println(t);
//        }
//        
//    }
//    
//    
//    
//  
//    
//    
//    
//    
//    public static String[] preparePK(String[] vos, boolean withPK){
//        String corpPk = SQLHelper.getCorpPk();
//        if (withPK) {
//            String[] pks = new String[vos.length];
//            int[] idx = new int[vos.length];
//            int length = 0;
//            for (int i = 0; i < vos.length; ++i) {
//                if (vos[i] == null) {
//                    continue;
//                }
//                String thePK = vos[i];
//                if ((thePK == null) || (thePK.trim().length() == 0))
//                    idx[(length++)] = i;
//                else {
//                    pks[i] = thePK;
//                }
//            }
//            if (length > 0) {
//                String[] npks = new Generator().generate(corpPk, length);
//                for (int i = 0; i < length; ++i) {
//                    vos[idx[i]] = (npks[i]);
//                    pks[idx[i]] = npks[i];
//                }
//            }
//            return pks;
//        }
//        String[] pks = new Generator().generate(corpPk, vos.length);
//        for (int i = 0; i < vos.length; ++i) {
//            if (vos[i] != null)
//                vos[i] = (pks[i]);
//            else {
//                pks[i] = null;
//            }
//        }
//        return pks;
//    }
//
//    
//
//
////    public static void selectTest() {
////        
////        
////          KuduAgent agent = new KuduAgent();
////          KuduColumn column01 = new KuduColumn();
////          column01.setColumnName("name");
////          column01.setColumnType(Type.STRING);
////          column01.setSelect(true);
////          column01.setComparisonOp(ComparisonOp.EQUAL);
////          column01.setComparisonValue("name_1");
////          
////          
////          KuduColumn column02 = new KuduColumn();
////          column02.setColumnName("id");
////          //column02.setSelect(true);
////          column02.setColumnType(Type.INT64);
////          
////          
////          KuduColumn column03 = new KuduColumn();
////          column03.setColumnName("sex");
////          column03.setSelect(true);
////          column03.setColumnType(Type.STRING);
////          
////          List<KuduColumn> list = new ArrayList<>();
////          list.add(column01);
////          list.add(column02);
////          list.add(column03);
////          
////          
////          List<Map<String, Object>> select = agent.select("impala::test.T_USER1", agent.client, list);
////          System.out.println("-----------------" + select);
////    }
//
//    public static void alterColumnTEST() throws KuduException {
//          KuduAgent agent = new KuduAgent();
//          KuduRow myrows01 = new KuduRow();
//          myrows01.setTableName("impala::test.T_USER1");
//          KuduColumn c01 = new KuduColumn();
//          c01.setColumnName("newsex");
//          c01.setNewColumnName("sex");
//          c01.setAlterColumnEnum(AlterColumnEnum.RENAME_COLUMN);
//          KuduColumn c02 = new KuduColumn();
//          c02.setColumnName("myadd");
//          c02.setAlterColumnEnum(AlterColumnEnum.DROP_COLUMN);
//          List<KuduColumn> rows01 = new ArrayList<>();
//          rows01.add(c01);
//          rows01.add(c02);
//          myrows01.setRows(rows01);
//        
//          KuduRow myrows11 = new KuduRow();
//          myrows11.setTableName("impala::impala_kudu.my_first_table");
//          KuduColumn c11 = new KuduColumn();
//          c11.setColumnName("newname");
//          c11.setNewColumnName("name");
//          c11.setAlterColumnEnum(AlterColumnEnum.RENAME_COLUMN);
//          KuduColumn c12 = new KuduColumn();
//          c12.setColumnName("nickName");
//          c12.setAlterColumnEnum(AlterColumnEnum.ADD_COLUMN);
//          c12.setNullAble(false);
//          c12.setColumnType(Type.STRING);
//          c12.setDefaultValue("aaa");
//          List<KuduColumn> rows11 = new ArrayList<>();
//          rows11.add(c11);
//          rows11.add(c12);
//          myrows11.setRows(rows11);
//        
//          List<KuduRow> list = new ArrayList<>();
//          list.add(myrows01);
//          list.add(myrows11);
//        
//          agent.alter(agent.client, list);
//    }
//
//    public static void renameTEST() throws KuduException {
//          KuduAgent agent = new KuduAgent();
//          KuduRow myrows01 = new KuduRow();
//          myrows01.setTableName("impala::impala_kudu.my_first_table");
//          myrows01.setNewTableName("impala::impala_kudu.my_first_table1");
//          myrows01.setAlterTableEnum(AlterTableEnum.RENAME_TABLE);
//        
//          KuduRow myrows02 = new KuduRow();
//          myrows02.setTableName("impala::impala_kudu.my_first_table1");
//          myrows02.setNewTableName("impala::impala_kudu.my_first_table");
//          myrows02.setAlterTableEnum(AlterTableEnum.RENAME_TABLE);
//        
//          List<KuduRow> list = new ArrayList<>();
//          list.add(myrows01);
//          list.add(myrows02);
//        
//          agent.alter(agent.client, list);
//    }
//
//    public static void deleteMultTEST() throws KuduException {
//          KuduAgent agent = new KuduAgent();
//          //绗竴琛�
//          KuduColumn c01 = new KuduColumn();
//          c01.setColumnName("id");
//          c01.setColumnValue(1000001);
//          c01.setColumnType(Type.INT64);
//          c01.setUpdate(false);
//          c01.setPrimaryKey(true);
//          KuduColumn c02 = new KuduColumn();
//          c02.setColumnName("name");
//          c02.setColumnValue("lijie123");
//          c02.setColumnType(Type.STRING);
//          c02.setUpdate(false);
//          List<KuduColumn> row01 = new ArrayList<>();
//          row01.add(c01);
//        //  row01.add(c02);
//          KuduRow myrows01 = new KuduRow();
//          myrows01.setRows(row01);
//        
//          //绗竴琛�
//          KuduColumn c11 = new KuduColumn();
//          c11.setColumnName("id");
//          c11.setColumnValue(1000002);
//          c11.setColumnType(Type.INT64);
//          c11.setUpdate(false);
//          c11.setPrimaryKey(true);
//          KuduColumn c12 = new KuduColumn();
//          c12.setColumnName("name");
//          c12.setColumnValue("lijie123");
//          c12.setColumnType(Type.STRING);
//          c12.setUpdate(false);
//          List<KuduColumn> row11 = new ArrayList<>();
//          row11.add(c11);
//        //  row11.add(c12);
//          KuduRow myrows11 = new KuduRow();
//          myrows11.setRows(row11);
//        
//          List<KuduRow> rows = new ArrayList<>();
//          rows.add(myrows01);
//          rows.add(myrows11);
//          agent.delete("impala::impala_kudu.my_first_table", agent.client, rows);
//    }
//
//    public static void deleteSingleTEST() throws KuduException {
//          KuduAgent agent = new KuduAgent();
//          //绗竴琛�
//          KuduColumn c01 = new KuduColumn();
//          c01.setColumnName("id");
//          c01.setColumnValue(1000003);
//          c01.setColumnType(Type.INT64);
//          c01.setUpdate(false);
//          c01.setPrimaryKey(true);
//          KuduColumn c02 = new KuduColumn();
//          c02.setColumnName("name");
//          c02.setColumnValue("lijie789");
//          c02.setColumnType(Type.STRING);
//          c02.setUpdate(false);
//          List<KuduColumn> row01 = new ArrayList<>();
//          row01.add(c01);
//        //  row01.add(c02);
//          KuduRow myrows01 = new KuduRow();
//          myrows01.setRows(row01);
//          agent.delete("impala::impala_kudu.my_first_table", agent.client, myrows01);
//    }
//
//    public static void updateMultTEST() throws KuduException {
//          KuduAgent agent = new KuduAgent();
//          //绗竴琛�
//          KuduColumn c01 = new KuduColumn();
//          c01.setColumnName("id");
//          c01.setColumnValue(1000001);
//          c01.setColumnType(Type.INT64);
//          c01.setUpdate(false);
//          c01.setPrimaryKey(true);
//          KuduColumn c02 = new KuduColumn();
//          c02.setColumnName("name");
//          c02.setColumnValue("lijie123");
//          c02.setColumnType(Type.STRING);
//          c02.setUpdate(false);
//          List<KuduColumn> row01 = new ArrayList<>();
//          row01.add(c01);
//          row01.add(c02);
//          KuduRow myrows01 = new KuduRow();
//          myrows01.setRows(row01);
//          //绗簩琛�
//          KuduColumn c11 = new KuduColumn();
//          c11.setColumnName("id");
//          c11.setColumnValue(1000002);
//          c11.setColumnType(Type.INT64);
//          c11.setUpdate(false);
//          c11.setPrimaryKey(true);
//          KuduColumn c12 = new KuduColumn();
//          c12.setColumnName("name");
//          c12.setColumnValue("lijie456");
//          c12.setColumnType(Type.STRING);
//          c12.setUpdate(false);
//          List<KuduColumn> row11 = new ArrayList<>();
//          row11.add(c11);
//          row11.add(c12);
//          KuduRow myrows11 = new KuduRow();
//          myrows11.setRows(row11);
//        
//          List<KuduRow> rows = new ArrayList<>();
//          rows.add(myrows01);
//          rows.add(myrows11);
//          agent.update("impala::impala_kudu.my_first_table", agent.client, rows);
//    }
//
//    public static void updateSingleTEST() throws KuduException {
//          KuduAgent agent = new KuduAgent();
//          //绗竴琛�
//          KuduColumn c01 = new KuduColumn();
//          c01.setColumnName("id");
//          c01.setColumnValue(12);
//          c01.setColumnType(Type.INT64);
//          c01.setUpdate(false);
//          c01.setPrimaryKey(true);
//          KuduColumn c02 = new KuduColumn();
//          c02.setColumnName("name");
//          c02.setColumnValue("lijie789");
//          c02.setColumnType(Type.STRING);
//          c02.setUpdate(false);
//          KuduColumn c03 = new KuduColumn();
//          c03.setColumnName("sex");
//          c03.setColumnValue("lijie789");
//          c03.setColumnType(Type.STRING);
//          c03.setUpdate(false);
//          List<KuduColumn> row01 = new ArrayList<>();
//          row01.add(c01);
//          row01.add(c02);
//          row01.add(c03);
//          KuduRow myrows01 = new KuduRow();
//          myrows01.setRows(row01);
//          agent.update("impala::impala_kudu.my_first_table", agent.client, myrows01);
//    }
//
//    public static void insertMultTEST() throws KuduException {
//          KuduAgent agent = new KuduAgent();
//          //绗竴琛�
//          KuduColumn c01 = new KuduColumn();
//          c01.setColumnName("id");
//          c01.setColumnValue(1000001);
//          c01.setColumnType(Type.INT64);
//          c01.setUpdate(false);
//          KuduColumn c02 = new KuduColumn();
//          c02.setColumnName("name");
//          c02.setColumnValue("lijie001");
//          c02.setColumnType(Type.STRING);
//          c02.setUpdate(false);
//          List<KuduColumn> row01 = new ArrayList<>();
//          row01.add(c01);
//          row01.add(c02);
//          KuduRow myrows01 = new KuduRow();
//          myrows01.setRows(row01);
//        
//          //绗簩琛�
//          KuduColumn c11 = new KuduColumn();
//          c11.setColumnName("id");
//          c11.setColumnValue(1000002);
//          c11.setColumnType(Type.INT64);
//          c11.setUpdate(false);
//          KuduColumn c12 = new KuduColumn();
//          c12.setColumnName("name");
//          c12.setColumnValue("lijie002");
//          c12.setColumnType(Type.STRING);
//          c12.setUpdate(false);
//          List<KuduColumn> row02 = new ArrayList<>();
//          row02.add(c11);
//          row02.add(c12);
//          KuduRow myrows02 = new KuduRow();
//          myrows02.setRows(row02);
//        
//          List<KuduRow> rows = new ArrayList<>();
//          rows.add(myrows01);
//          rows.add(myrows02);
//        
//          agent.insert("impala::impala_kudu.my_first_table", agent.client, rows);
//    }
//
//    public static void insertSingleTEST() throws KuduException {
//          KuduAgent agent = new KuduAgent();
//          //绗竴琛�
//          KuduColumn c01 = new KuduColumn();
//          c01.setColumnName("id");
//          c01.setColumnValue(1000003);
//          c01.setColumnType(Type.INT64);
//          c01.setUpdate(false);
//          
//          KuduColumn c02 = new KuduColumn();
//          c02.setColumnName("name");
//          c02.setColumnValue("lijie003");
//          c02.setColumnType(Type.STRING);
//          c02.setUpdate(false);
//          
//          List<KuduColumn> row01 = new ArrayList<>();
//          row01.add(c01);
//          row01.add(c02);
//          KuduRow myrows01 = new KuduRow();
//          myrows01.setRows(row01);
//          agent.insert("impala::test.t_user1", agent.client, myrows01);
//    }
//}
