package com.ming.demo

import com.ming.hbase.client.HbaseClient
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object HDFSCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("HDFS Word Count")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(2))
    val word = ssc
      .textFileStream("hdfs://localCDH1:8020/data/word.txt")
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
    word.foreachRDD(rdd => {
      rdd.foreachPartition(iter => {
        iter.foreach(line => {
          HbaseClient.putData("word", "HDFS", "info", line._1, String.valueOf(line._2))
        })
      })
    })
    ssc.start()
    ssc.awaitTermination()
  }
}
