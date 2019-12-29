package ming.shui.demo.streaming

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object StreamingWordCount {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
        .setAppName("word-count")
        .setMaster("local[*]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val ssc = new StreamingContext(sc, Seconds(5))
    val res = ssc.socketTextStream("localhost",9999)
        .flatMap(_.split(" "))
        .map((_, 1))
        .reduceByKey(_ + _)
        .print()
    ssc.awaitTermination()
    ssc.stop()
  }

}
