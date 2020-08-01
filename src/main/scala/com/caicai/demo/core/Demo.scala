package com.caicai.demo.core

import scala.io.Source
import scala.util.parsing.json.JSON


object Demo {
  /**
    * 解析IP地址，得到国家、省份、城市信息
    * @param ip ip地址
    * @return （国家、省份、城市）三元组
    */
  def parseIP(ip: String): (String, String, String) = {
    val url = "https://m.so.com/position?ip=" + ip
    val json = Source.fromURL(url).getLines().next()
    val data = JSON.parseFull(json).getOrElse("data", "null").asInstanceOf[Map[String, Object]].getOrElse("data", "null").asInstanceOf[Map[String, Object]].getOrElse("position","null").asInstanceOf[Map[String, Object]]
    val country = data.getOrElse("country", "null").asInstanceOf[String]
    val province = data.getOrElse("province", "null").asInstanceOf[String]
    val city = data.getOrElse("city", "null").asInstanceOf[String]
    (country, province, city)
  }
  def main(args: Array[String]): Unit = {
    val tup = parseIP("192.168.81.131")
    println(tup)
  }
}
