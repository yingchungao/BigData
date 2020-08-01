package com.caicai.demo.core

trait Abstract {
  type T
  def transform(x : T): T
  val initial: T
  var esx: T
}
