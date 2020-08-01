package com.caicai.demo.streaming

import java.text.SimpleDateFormat

import com.ynet.source.KafkaSource
import com.ynet.util.Property
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka.HasOffsetRanges
import org.apache.spark.streaming.kafka010.HasOffsetRanges
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.util.parsing.json.JSON

/**
  * 读取239kafka的数据，解析后写入kudu
  */
case class UserBehavior(user_id: String, pro_id: String, pro_name: String, category: String, action: Int)
object GateConsumerDataFromKafka {
  Logger.getLogger("org").setLevel(Level.WARN)
  private val topics = Property.getStrValue("kafka.topic.name4")
  private val brokerList = Property.getStrValue("kafka.bootstrap.servers4")
//  private val kuduUserInsightDetail =  Property.getStrValue("kudu.table.name.user.insight.detail")
//  private val kuduUserInsightSummary = Property.getStrValue("kudu.table.name.user.insight.summary")
//  private val kuduMasters  = Property.getStrValue("kudu.master")
  private var offsetRanges: HasOffsetRanges = null
  private val group = "gate-local-123457891"

  def main(args: Array[String]): Unit = {
    //初始化spark环境
    val spark = SparkSession
      .builder()
      .appName("gate detail kudu")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()
    val ssc = new StreamingContext(spark.sparkContext, Seconds(2))

    val xwhatSet = Set("click_finance_item",
      "click_btn_collection",
      "click_btn_confirm_purchase")

    //初始化kudu环境
//    val kuduContext = new KuduContext(kuduMasters, spark.sparkContext)
    //获取数据源
    val stream = KafkaSource
      .loadDataFromHigherKafka(topics, group, brokerList, "latest", ssc)
    //DStream转换操作
    val detail = stream
      .map(_.value())
      .map(json => JSON.parseFull(json).get.asInstanceOf[List[Map[String, Object]]])
      .flatMap(x => x)
      .filter(x => {
        val appid = x.getOrElse("appid", "null").asInstanceOf[String]
        val xwhat = x.getOrElse("xwhat", "null").asInstanceOf[String]
        val a = appid.equals("dev_test_application_id")
        a && xwhatSet.contains(xwhat)
      })
      .map(map => {
        val user_id = map.getOrElse("xwho", "null").asInstanceOf[String]
        val xwhat = map.getOrElse("xwhat", "null").asInstanceOf[String]
        val xwhen = map.getOrElse("xwhen", 0).asInstanceOf[Double]
        val xcontext = map.getOrElse("xcontext", null).asInstanceOf[Map[String, Object]]
        val date = new SimpleDateFormat("yyyyMMdd").format(xwhen)
        val proId = xcontext.getOrElse("_fi_aaa", "null").asInstanceOf[String]
        val category = xcontext.getOrElse("_fi_aab", "null").asInstanceOf[String]
        val proName = xcontext.getOrElse("_fi_aac", "null").asInstanceOf[String]
        val fiaah = xcontext.getOrElse("_fi_aah", null)
        val collection = if (fiaah.isInstanceOf[String]) {
          fiaah.asInstanceOf[String]
        } else if (fiaah.isInstanceOf[Int]) {
          fiaah.asInstanceOf[Int].toString
        }

        val score = xwhat match {
          //点击 1分
          case "click_finance_item" => 1
          // 购买 3分
          case "click_btn_confirm_purchase" => 3
          //收藏和取消收藏
          case "click_btn_collection" if (collection.equals("1")) => 2
          case "click_btn_collection" if (collection.equals("0")) => -2
          case _ => 0
        }
        (date, user_id, proId, proName, category, score)
      })

      .foreachRDD(rdd => {
        import spark.implicits._
        val df = rdd.map(t => {
          val date = t._1
          UserBehavior(t._2, t._3,  t._4, t._5, t._6)
        }).toDF()

        df.write.insertInto("u_behavior.origin_user_behavior")
      })
      //过滤掉不规则的json
//      .filter(x => {
//        !JSON.parseFull(x).isEmpty &&
//          x.contains("profile")
//      })
//      .map(json => {
//        JSON.parseFull(json).get.asInstanceOf[List[Map[String, Object]]]
//      })
//      .flatMap(x => x)
//      .map(p => {
//        val xwho = p.getOrElse("xwho", "null").asInstanceOf[String]
//        val appid = p.getOrElse("appid", "null").asInstanceOf[String]
//        val xwhat = p.getOrElse("xwhat", "null").asInstanceOf[String]
//        val xwhen = p.getOrElse("xwhen", "null").asInstanceOf[Double]
//        val dat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(xwhen)
//        (xwho, xwhat, dat, appid, p.getOrElse("xcontext", null))
//      })
//      .filter(p => {
////        p._1.equals("100000001796") || p._1.equals("100000001072")
//        p._2.equals("&profile_set") || p._2.contains("")
//      })


//    detail.filter(_._1 == 1).map(_._2).foreachRDD(SaveKudu.kuduDUserBaseInfoFunc(spark, kuduContext, "impala::dtpf2.d_user_base_info_1", _))
//    detail.filter(_._1 == 2).map(_._2).foreachRDD(SaveKudu.kuduDUserBaseInfoFunc(spark, kuduContext, "impala::dtpf2.d_user_base_info_2", _))
    ssc.start()
    ssc.awaitTermination()
  }

}
