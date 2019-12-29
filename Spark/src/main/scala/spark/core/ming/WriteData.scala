package spark.core.ming

import java.sql.DriverManager

import org.apache.hadoop.io.compress.GzipCodec
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.parsing.json.{JSONArray, JSONObject}
case class Person(name: String, age: Int)
object WriteData {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("WriteData").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //1.写入MySQL数据库
    //PrepareStatement和Connection不能跨分区实例化
    //RDD中有多个分区，一个分区会产生一个任务进程，多个分区可能会分散到多个不同的任务节点上
    //useUnicode=true&characterEncoding=utf8
    Class.forName("com.mysql.jdbc.Driver")
    val data1 = sc.parallelize(List(("老刘", 27), ("老高", 26)))
    data1.foreachPartition((iter: Iterator[(String, Int)]) => {
      val conn = DriverManager.getConnection("jdbc:mysql://linux01:3306/syllabus?useUnicode=true&characterEncoding=utf8",
        "root", "root")
      conn.setAutoCommit(false)
      val preparedStatement = conn.prepareStatement("insert into syllabus.person('name', 'age') values(?, ?);")
      iter.foreach(t => {
        preparedStatement.setString(1, t._1)
        preparedStatement.setInt(2, t._2)
        preparedStatement.addBatch()
      })
      preparedStatement.executeBatch()
      conn.commit()
      conn.close()
    })
    //2.保存成object文件
    val p1 = new Person("小明", 27)
    val p2 = new Person("小春", 26)
    val data2 = sc.parallelize(List(p1, p2), 1)
    val path2 = "file:///g:\\bookdata\\object"
    data2.saveAsObjectFile(path2)
    //3.保存成sequenceFile
    val data = List(("姓名", "小明"), ("年龄", "18"))
    val data3 = sc.parallelize(data)
    val path3 = "file:///g:\\bookdata\\sequencefile"
    data3.saveAsSequenceFile(path3, Some(classOf[GzipCodec]))
    //4.保存成CSV、TSV文件
    val array = Array("ming", 27, "male", "70kg", "183cm")
    val csvRDD = sc.parallelize(Array(array.mkString(",")), 1)
    val csvPath = "file:///g:\\bookedata\\csvpath"
    csvRDD.saveAsTextFile(csvPath)

    val tsvRDD = sc.parallelize(Array(array.mkString("\t")), 1)
    val tsvPath = "file:///g:\\bookdata\\tsvpath"
    tsvRDD.saveAsTextFile(tsvPath)
    //5.保存成JSON文件
    val map1 = Map("name" -> "ming", "age" -> 27, "address" -> JSONArray(List("地址1", "地址2")))
    val map2 = Map("name" -> "chun", "age" -> 26, "address" -> JSONArray(List("地址1", "地址2", "地址3")))
    val data5 = sc.parallelize(List(JSONObject(map1), JSONObject(map2)))
    val path5 = "file:///g:\\bookedata\\json"
    data5.saveAsTextFile(path5)
    //6.保存成普通文件
    val data6 = sc.parallelize(Array(("one", 1), ("two", 2), ("three", 3)), 3)
    val path6 = "file:///g:\\bookdata\\txt"
    data6.saveAsTextFile(path6)

    sc.stop()
  }

}
