package com.ming.spark.core

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * 测试SparkSQL读写Hive表数据
  * liuming
  * 2020/3/16 10:24
  */
object SparkSQLDev {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName("spark-sql-dev")
      .setMaster("spark://192.168.65.239:7077")

    val spark = SparkSession
      .builder()
      .config(conf)
      // 指定hive的metastore的端口  默认为9083 在hive-site.xml中查看
      .config("hive.metastore.uris", "thrift://192.168.65.239:9083")
      //指定hive的warehouse目录
      .config("spark.sql.warehouse.dir", "hdfs://192.168.65.239:8020/user/hive/warehouse")
      .enableHiveSupport()
      .getOrCreate()

    spark.sql("show databases;").show(10)

    spark.stop()
  }

}
