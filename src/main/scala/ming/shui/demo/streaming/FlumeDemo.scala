package ming.shui.demo.streaming

import java.net.InetSocketAddress

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.flume.{FlumeUtils, SparkFlumeEvent}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object FlumeDemo {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("flume-demo")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(30))

    val flumeAddress = Seq(new InetSocketAddress("linux01", 9999))
    val flumeEventDStream: ReceiverInputDStream[SparkFlumeEvent] = FlumeUtils.createPollingStream(
      ssc,
      flumeAddress,
      StorageLevel.MEMORY_AND_DISK_SER_2
    )
    val flumeDStream = flumeEventDStream
      .map(s => new String(s.event.getBody.array()))
        .map(u => (u.split(",")(0), 1))
        .reduceByKey(_ + _)
        .print()
    ssc.start()
    ssc.awaitTermination()
    ssc.stop()
  }

}
