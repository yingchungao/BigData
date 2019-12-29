package spark.core.ming

import java.sql.DriverManager

import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 读取数据并生成RDD
  */
object ReadData {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("ReadData")
    val sc = new SparkContext(conf)
    //1.读取MySQL数据库中的数据
    val inputMysql = new JdbcRDD(sc,
      () => {
        Class.forName("com.mysql.jdbc.Driver")
        DriverManager.getConnection("jdbc:mysql://linux01:3306/syllabus?useUnicode=true&characterEncoding=utf8",
        "root", "root")
      },
    "select * from person where id >= ? and id <= ?;",
      1,3,1,
      r => (r.getInt(1), r.getString(2), r.getInt(3)))
    println("查询到的记录条目数：" + inputMysql.count())
    inputMysql.foreach(println _)
    //2.读取object格式的数据
    //读取object格式的数据所使用的样例类，需要与保存该数据时所使用的样例类相同
    //包括包名也要一致，否则无法解析
    val path1 = "file:///g:\\bookdata\\data.object"
    val data1 = sc.objectFile[Person](path1)
    println(data1.collect.toList)
    //3.读取SequenceFile格式的数据
    //只有键值对的数据才能用SequenceFile格式存储
    val path2 = "file:///g:\\bookdata\\data.sequence"
    val data2 = sc.sequenceFile[String, String](path2)
    println(data2.collect.mkString(","))
    //4.读取CSV TSV格式的数据
    val data3 = sc.textFile("g:\\bookdata\\data.csv").flatMap(_.split(",")).collect
    data3.foreach(println _)
    val data4 = sc.textFile("g:\\bookdata\\data.tsv").flatMap(_.split("\t")).collect
    data4.foreach(println _)
    //5.读取JSON格式的数据
    val data5 = sc.textFile("g:\\bookdata\\data.json")
    val content = data5.map(JSON.parseFull(_))
    println(content.collect.mkString("\t"))
    content.foreach(
      {
        case Some(map: Map[String, Any]) => println(map)
        case None => println("无效的JSON")
        case _ => println("其他异常")
      }
    )
    //6.读取普通文本数据
    val data6 = sc.textFile("g:\\bookdata\\data.txt")
    println(data6.collect.mkString(","))
    sc.stop()
  }

}
