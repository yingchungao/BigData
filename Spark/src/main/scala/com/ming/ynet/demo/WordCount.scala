package com.ming.ynet.demo

import com.ming.hbase.client.HbaseClient
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010._

object WordCount {

  def main(args: Array[String]): Unit = {

    //获取参数
    val Array(brokers, groupId, topics) = args

    // 初始化StreamingContext
    val conf = new SparkConf().setMaster("local[*]").setAppName("DirectKafkaWordCount")
    val ssc = new StreamingContext(conf, Seconds(2))

    // kafka topic 主题
    val topicsSet = topics.split(",").toSet

    // kafka参数设置
    val kafkaParams = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
      ConsumerConfig.GROUP_ID_CONFIG -> groupId,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
    )

    // straming 读取HDFS数据
    val hdfsLines = ssc.textFileStream("hdfs://192.168.189.128:8020/data/word.txt")
    val hdfsResult = hdfsLines.flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)

    // 读取kafka数据
    val kafkaMessages = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topicsSet, kafkaParams))

    val kafkaResult = kafkaMessages
      .map(_.value())
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)

    // 聚合操作
    val result = hdfsResult
      .join(kafkaResult)
      .map(t => {
        t._1 + "," + t._2._1 + "," + t._2._2
      })

    // 写入HBase
    result.foreachRDD(rdd => {
      rdd.foreachPartition(iter => {
        val tableName = "WordCount"
        iter.foreach(line => {
          val Array(word, v1, v2) = line.split(",")
          HbaseClient.putData(tableName, word, "count", "HDFS", v1)
          HbaseClient.putData(tableName, word, "count", "kafka", v2)
        })
      })
    })
    ssc.start()
    ssc.awaitTermination()
  }

}
