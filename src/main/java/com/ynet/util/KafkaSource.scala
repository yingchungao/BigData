package com.ynet.util

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream

object KafkaSource {
  /**
    * 获取高版本Kafka中的数据
    * 这里可以进行优化，
    * 如kafka分区设置，缓冲区大小，读取时间间隔等
    * @param topics 主题
    * @param group 消费者组
    * @param brokerList 节点列表
    * @param offset 指定从哪里开始消费。latest or earliest
    * @param ssc StreamingContext
    * @return DStream
    */
  def loadDataFromHigherKafka(topics: String, group: String, brokerList: String, offset: String, ssc: StreamingContext): InputDStream[ConsumerRecord[String, String]]= {
    val kafkaParams = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokerList,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
      ConsumerConfig.GROUP_ID_CONFIG -> group,
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> offset,
      ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> (false: java.lang.Boolean)
    )
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](List(topics), kafkaParams)
    )
    stream
  }
}
