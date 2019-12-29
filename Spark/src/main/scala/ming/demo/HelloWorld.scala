package ming.demo

import java.io.{FileNotFoundException, FileReader, IOException}

import org.apache.spark.{SparkConf, SparkContext}

object HelloWorld {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("word").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val fileRDD = sc.textFile("D:/word.txt")

    val rdd1 = sc.parallelize(List(1,2,3,4,5,6,7,8,9))
    val rdd2 = sc.parallelize(List(6,7,8,9,10,111,12,13,14))
    rdd2.subtract(rdd1).foreach(println _)


    fileRDD.flatMap(_.split(" ")).map((_, 1)).mapValues(t => t * 11).reduceByKey(_ + _).foreach(println _)


    fileRDD.glom().foreach(arr => {
      arr.foreach(println _)
      println("------------")
    })

    fileRDD.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).foreach(println _)

    sc.stop()
  }
}
