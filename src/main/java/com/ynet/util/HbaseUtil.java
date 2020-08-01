package com.ynet.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.*;

/**
 * Hbase 客户端
 * 完成对hbase的增删改查操作
 */
public class HbaseUtil {
    private static Admin admin;
    public static Connection conn;

    static {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.root.dir", "hdfs://192.168.65.239:8020/hbase");
        conf.set("hbase.zookeeper.quorum", "192.168.65.239:2181");
        conf.set("hbase.client.scanner.timeout.period", "1000");
        conf.set("hbase.rpc.timeout", "1000");
        try {
            conn = ConnectionFactory.createConnection(conf);
            admin = conn.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户在指定日期范围内的事件点击次数
     * @param userId 用户ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param eventName 事件名称
     * @return 该事件在每一天的点击次数
     */
    public static List<Map<String, Integer>> getUserStart2EndEventClickData(String userId, String startDate, String endDate, String eventName) throws Exception{

        List<Map<String, Integer>> list = new ArrayList<>();

        Table table = conn.getTable(TableName.valueOf("pay_statisv2"));
        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes(userId + "," + startDate));
        scan.setStopRow(Bytes.toBytes(userId + "," + endDate));

        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            String rowkey = new String(result.getRow());
            String date = rowkey.split(",")[1];
            Map<String, Integer> map = new HashMap<>();
            for (Cell cell : result.rawCells()) {
                String event = new String(CellUtil.cloneQualifier(cell));
                if (event.equals(eventName)) {
                    int value = Integer.parseInt(new String(CellUtil.cloneValue(cell)));
                    map.put(date, value);
                }
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 获取用户在指定时间范围内所有时间的点击次数
     * @param userId 用户ID
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 这个用户在每一天的点击总次数
     */
    public static List<Map<String, Integer>> getUserStart2EndAllClickData(String userId, String startDate, String endDate) throws Exception{

        List<Map<String, Integer>> list = new ArrayList<>();

        Table table = conn.getTable(TableName.valueOf("UserInsightSummary"));
        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes(userId + "," + startDate));
        scan.setStopRow(Bytes.toBytes(userId + "," + endDate));

        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            String rowkey = new String(result.getRow());
            String date = rowkey.split(",")[1];
            Map<String, Integer> map = new HashMap<>();
            for (Cell cell : result.rawCells()) {
                int value = Integer.parseInt(new String(CellUtil.cloneValue(cell)));
                if (map.containsKey(date)) {
                     int preValue = map.get(date);
                     map.put(date, value + preValue);
                } else {
                    map.put(date, value);
                }
            }
            list.add(map);
        }
        return list;
    }

    public static void main(String[] args) throws Exception {

//        List<Map<String, Integer>> list = getUserStart2EndAllClickData("69363045-DF42-49C3-B15F-CFE71FEC6432",
//                "2020-01-13", "2020-01-13");
//        for(Map<String, Integer> map : list) {
//            for (Map.Entry<String, Integer> entry : map.entrySet()) {
//                System.out.println("--------------" + entry.getKey() + ":" + entry.getValue() + "-----------------------");
//            }
//        }
//        System.out.println("=============================================================");
//        List<Map<String, Integer>> list2 = getUserStart2EndEventClickData("69363045-DF42-49C3-B15F-CFE71FEC6432",
//                "2020-01-13", "2020-01-13" ,"pv");
//        for(Map<String, Integer> map : list2) {
//            for (Map.Entry<String, Integer> entry : map.entrySet()) {
//                System.out.println("--------------" + entry.getKey() + ":" + entry.getValue() + "-----------------------");
//            }
//        }

        Table table = conn.getTable(TableName.valueOf("UserInsightSummary"));
        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes("69363045-DF42-49C3-B15F-CFE71FEC6432,2020-01-13"));
        scan.setStopRow(Bytes.toBytes("WjHe1axCYLQDABvLIDqQ9l2D,2020-01-13"));

        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            System.out.println("rowkey:" + new String(result.getRow()));
            for (Cell cell : result.rawCells()) {
                System.out.println(
                        " 行为:" + new String(CellUtil.cloneQualifier(cell)) +
                                ",点击次数:" + new String(CellUtil.cloneValue(cell)));
            }
        }

//        String v = getData("demo", "刘明", "info", "clickTimes");
//        System.out.println(v);

//        String str = new String("刘明的".getBytes(), "utf-8");
//        putData("demo", str, "info", "clickTimes", "1");
//        Table table = conn.getTable(TableName.valueOf("demo"));
//        Put put = new Put(Bytes.toBytes(str));
//        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("clickTim"), Bytes.toBytes(String.valueOf(1)));
//        table.put(put);
//
//
//        String tableName = "pay_statisv2";
//        String rowkey = "69363045-DF42-49C3-B15F-CFE71FEC6432,2020-01-13";
//        String familyName = "info";
//        String column = "pv";
//
//        String data1 = getData(tableName, rowkey, familyName, column);
//        System.out.println(data1);
//
//        List<String> allKey = getAllKey(tableName);
//        for(String ip : allKey) {
//            System.out.println(ip);
//        }

    }
    
    public static void createTable(String tableName, String... columnFamilies) throws IOException {
        TableName tablename = TableName.valueOf(tableName);
        if (admin.tableExists(tablename)) {
            System.out.println("Table Exists");
        } else {
            System.out.println("Start create table");
            HTableDescriptor tableDescriptor = new HTableDescriptor(tablename);
            for (String columnFamliy : columnFamilies) {
                HTableDescriptor column = tableDescriptor.addFamily(new HColumnDescriptor(columnFamliy));
            }
            admin.createTable(tableDescriptor);
            System.out.println("Create Table success");
        }
    }

    /**
     * 获取一列获取一行数据
     *
     * @param tableName
     * @param rowKey
     * @param famliyName
     * @param column
     * @return
     * @throws IOException
     */
    public static String getData(String tableName, String rowKey, String famliyName, String column) throws IOException {
        Table table = conn.getTable(TableName.valueOf(tableName));
        byte[] row = Bytes.toBytes(rowKey);
        Get get = new Get(row);
        Result result = table.get(get);
        byte[] resultValue = result.getValue(famliyName.getBytes(), column.getBytes());
        if (null == resultValue) {
            return null;
        }
        return new String(resultValue);
    }
    /**
     * 向对应列添加数据
     *
     * @param tablename  表名
     * @param rowkey     行号
     * @param famliyname 列族名
     * @param column     列名
     * @param data       数据
     * @throws Exception
     */
    public static void putData(String tablename, String rowkey, String famliyname, String column, String data) throws Exception {
        Table table = conn.getTable(TableName.valueOf(tablename));
        Put put = new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes(famliyname), Bytes.toBytes(column), Bytes.toBytes(data));
        table.put(put);
    }

    /**
     *  增量更新点击次数
     *
     * @param tablename  表名
     * @param rowkey     行号
     * @param famliyname 列族名
     * @param column     列名
     * @throws Exception
     */
    public static void increamColumnData(String tablename, String rowkey, String famliyname, String column, int data) throws Exception {
        String val = getData(tablename, rowkey, famliyname, column);
        if (val != null) {
            data += Integer.valueOf(val);
        }
        putData(tablename, rowkey, famliyname, column, String.valueOf(data));
    }

    /**
     * 取出表中所有的key
     *
     * @param tableName
     * @return
     */
    public static List<String> getAllKey(String tableName) throws IOException {
        List<String> keys = new ArrayList<>();
        Scan scan = new Scan();
        Table table = HbaseUtil.conn.getTable(TableName.valueOf(tableName));
        ResultScanner scanner = table.getScanner(scan);
        for (Result r : scanner) {
            keys.add(new String(r.getRow()));
        }
        return keys;
    }
    /**
     * 获取一行的所有数据 并且排序
     *
     * @param tableName 表名
     * @param rowKey    列名
     * @throws IOException
     */
    public static List<Map.Entry> getRow(String tableName, String rowKey) throws IOException {
        Table table = conn.getTable(TableName.valueOf(tableName));
        byte[] row = Bytes.toBytes(rowKey);
        Get get = new Get(row);
        Result r = table.get(get);

        HashMap<String, Double> rst = new HashMap<>();

        for (Cell cell : r.listCells()) {
            String key = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
            String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
            rst.put(key, new Double(value));
        }

        List<Map.Entry> ans = new ArrayList<>();
        ans.addAll(rst.entrySet());


        Collections.sort(ans, (m1, m2) -> new Double((Double)m2.getValue() * 100000000 - (Double)m1.getValue() * 100000000).intValue());
//        Collections.sort(ans, (m1, m2) -> new Double((Double) m2.getValue() - (Double) m1.getValue()).intValue());

        return ans;
    }

}

