package com.caicai.demo.core

import org.apache.spark.{SparkConf, SparkContext}

object RDDdemo {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("demo").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val rdd1 = sc
      .parallelize(Array(("c1", 97.5),("c1", 99.5),
        ("c2", 98.0),("c1", 100.0),("c2", 96.5)))

    sc.setLogLevel("WARN")

    val rdd = rdd1.combineByKey(
      grade => (grade, 1),
      (gc: (Double, Int), grade) => (gc._1 + grade, gc._2 + 1),
      (gc1: (Double, Int), gc2: (Double, Int)) => (gc1._1 + gc2._1, gc1._2 + gc2._2)
    ).map(t => (t._1, t._2._1 / t._2._2))
      .foreach(println _)


    val f = (t:(Double, Int)) => t._1 / t._2
    val result = rdd1
      .mapValues(t => (t, 1))
      .reduceByKey((t1: (Double,Int), t2: (Double, Int)) => (t1._1 + t2._1, t1._2 + t2._2))
      .mapValues((t: (Double, Int)) => t._1 / t._2)
      .foreach(println _)

    sc.stop()
  }

}
