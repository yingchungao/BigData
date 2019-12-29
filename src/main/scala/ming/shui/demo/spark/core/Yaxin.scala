package ming.shui.demo.spark.core

import org.apache.spark.{SparkConf, SparkContext}

object Yaxin {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("demo")
    val sc = new SparkContext(conf)

    val data = Array(("u01", "h01"),
      ("u01", "h02"),
      ("u02", "h01"),
      ("u02", "h02"),
      ("u03", "h01"),
      ("u04", "h04"))
    val rdd = sc.parallelize(data)
    val res = rdd.mapValues(t => (t, 1))
        .reduceByKey((t1, t2) => (t1._1, t2._2 + t1._2))
        .filter(t => t._2._2 == 1)
        .mapValues(t => t._1)
        .foreach(println _)
    sc.stop()
  }

}
