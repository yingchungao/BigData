package ming.demo

import java.io.{File, PrintWriter}

import scala.io.Source

object WordCount {
  def main(args: Array[String]): Unit = {

    val file = new File("d:\\data.txt")
    withPrintWriter(file){writer =>
      writer.println(new java.util.Date)
    }



    val map = Map(1 -> "s")
    val mainList = List(3, 2, 1)
    val with4 = 42 :: mainList.tail

    val when  = "AM" :: "PM" :: List()
    print(when)
    
    val days = List("Sunday", "Monday", "Tuedsay", "Wedensday", "Thursday", "Friday", "Saturday")
    println(days.mkString(":"))
    days match {
      case firstDay :: otherDays =>
        println("The first day of the week is:" + firstDay)
      case List() =>
        println("XXXX")
    }

  }
  def withPrintWriter(file: File)(op: PrintWriter => Unit) = {
    val writer = new PrintWriter(file)
    try {
      op(writer)
    } finally {
      writer.close()
    }
  }
}
