package com.ming.spark.core

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      //设置任务名称
      .setAppName("WordCount")
      //设置目标master通信地址
      .setMaster("spark://linix01:7077")
    //实例化SparkContext，Spark的对外接口负责用户与Spark内部的交互通信
    val sc = new SparkContext(conf)
    //读取文件并进行单词统计
    sc.textFile("hdfs://linux01:8020/word.txt")
        .flatMap(_.split(" "))
        .map((_, 1))
        .reduceByKey(_ + _)
        .sortBy(_._2, false)
        .saveAsTextFile("hdfs://linux01:8020/output")
    // 停止sc，结束该任务
    sc.stop()
  }

}
