package com.ming.spark.core

import java.text.SimpleDateFormat
import java.util.{Date, Timer, TimerTask}

/**
  * liuming
  * 2020/3/18 10:10
  */
object SparkContextDEv {
  def main(args: Array[String]): Unit = {

    val timer = new Timer("ming", true)

    val task = new TimerTask {
      override def run(): Unit = {
        val date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
        println(date)
      }
    }
    timer.schedule(task,2000,1000)

  }
}
