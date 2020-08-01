package com.caicai.demo.core

import scala.io.Source

/**
  * 查找ip归属地
  */
object IpFromUtils {

  def main(args: Array[String]): Unit = {
    val ip = ipToLong("12.216.227.251")
    println(ip)
    val tuples: Array[(Long, Long, String)] = rules("E:\\ip.txt")

    val result: Int = binarySearch(tuples, ip)

    if (result == -1)
      return

    val ipForm: (Long, Long, String) = tuples(result)

    println(ipForm)
  }

  /**
    * ip转换10进制
    *
    * @param ip
    * @return
    */
  def ipToLong(ip: String): Long = {

    val ipArr = ip.split("[.]")

    var ipNum = 0L;

    for (i <- ipArr) {
      // | ： 按位或运算 ，位运算符，相同位数主要出现1都为1，如（1|1=1,1|0=1,0|1=1）
      // ^ : 异或运算符，2数相同为0，不同为1，如（0^0=0,1^1=0）
      ipNum = i.toLong | ipNum << 8
    }

    ipNum
  }

  /**
    * 生成规则
    *
    * @param filePath
    * @return
    */
  def rules(filePath: String): Array[(Long, Long, String)] = {
    val path = Source.fromFile(filePath.toString())

    val array: Array[(Long, Long, String)] = path.getLines().map(lines => {
      val strArr = lines.split("[|]")
      val ipNum1 = ipToLong(strArr(0))
      val ipNum2 = ipToLong(strArr(1))
      val provnice = strArr(4)
      (ipNum1, ipNum2, provnice)
    }).toArray

    array
  }

  def generalRules(line : String): (Long, Long, String)={
    val strArr = line.split("[|]")
    val ipStart = strArr(0).toLong
    val ipEnd = strArr(1).toLong
    val province = strArr(4)
    (ipStart, ipEnd, province)
  }

  /**
    * 二分法(前提有序)
    */
  def binarySearch(array: Array[(Long, Long, String)], num: Long): Int = {
    var end = array.length - 1;
    var begin = 0;

    while (begin <= end) {
      val middle = (begin + end) >> 1;


      if (num >= array(middle)._1 && num <= array(middle)._2) {
        return middle
      }

      if (num < array(middle)._1) {
        end = middle - 1
      }
      else {
        begin = middle + 1
      }

    }

    -1
  }
}