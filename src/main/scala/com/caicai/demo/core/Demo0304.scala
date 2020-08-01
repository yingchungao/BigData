package com.caicai.demo.core

/**
  * liuming
  * 2020/3/4 10:20
  */
case class Person(name: String, isMale: Boolean, children: Person*)
object Demo0304 {
  def main(args: Array[String]): Unit = {
    val set = scala.collection.immutable.TreeSet.empty[Int]
    val s = set + 1 + 1 + 3 + 3 + 2

    val c = collection.immutable.IndexedSeq(1, 2, 3)
    println(c)
    val lt = List(1,2)
    val l2 = 4 +: lt :+ 3
    println(9 :: l2)
    val str = 1 #:: 2 #:: 3 #:: Stream.empty
    println(str)
    val m = Map("ming" -> 1, "chun" -> 2)

    val lara = Person("Lara", false)
    val bob = Person("Bob", true)
    val julie = Person("Julie", false, lara, bob)
    val persons = List(lara, bob, julie)

    val t1 = persons.filter(!_.isMale).flatMap(p => {
      p.children.map(c => (p.name,c.name))
    })
    println(t1)
    println("===============================")
    val t2 = for (p <- persons; if !p.isMale; c <- p.children)
      yield (p.name, c.name)
    println(t2)
    val fib = fibFrom(1, 1).take(7)
    println(fib)
  }
  def fibFrom(a: Int, b: Int): Stream[Int] = a #:: fibFrom(b, a + b)
}
