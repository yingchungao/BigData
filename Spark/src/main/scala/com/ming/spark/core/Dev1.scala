package com.ming.spark.core
/**
  * liuming
  * 2020/3/23 16:27
  */
object Dev1 {

  def main(args: Array[String]): Unit = {
    var mainClass: Class[_] = null
    try {
      mainClass = Class.forName("com.ming.spark.core.WorkerState")
      val mainMethod = mainClass.getMethod("main", new Array[String](0).getClass)
      mainMethod.invoke(null, args)
    } catch {
      case e: Exception => println("dd")
    }
  }

}
