package com.ming.shui.demo.streaming

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object StreamingDemo {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("streaming")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val ssc = new StreamingContext(sc, Seconds(2))

    val lines = ssc.socketTextStream("localhost", 9999)
    val result = lines
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
      .print()
    ssc.start()
    ssc.awaitTermination()


    ssc.awaitTermination()
    sc.stop()
  }

}
