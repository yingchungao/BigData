package com.ming.spark.core

/**
  * liuming
  * 2020/3/23 16:11
  */
object WorkerState extends Enumeration {

  type WorkerState = Value

  val ALIVE, DEAD, DECOMMISSIONED, UNKNOW = Value

  def main(args: Array[String]): Unit = {
    println("hello world!~")
    println(WorkerState.DECOMMISSIONED)
  }

}
