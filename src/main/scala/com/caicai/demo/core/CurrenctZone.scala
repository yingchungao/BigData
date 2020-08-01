package com.caicai.demo.core

abstract class CurrenctZone {
  type Currency <: AbstractCurrency
  def make(x: Long): Currency
  abstract class AbstractCurrency {
    val amount: Long
    def designation: String
    def + (that: Currency): Currency = make(this.amount + that.amount)
    def * (x: Double): Currency = make((this.amount * x).toLong)
    def - (that: Currency): Currency = make(this.amount - that.amount)
    def / (that: Double): Currency = make((this.amount / that).toLong)
    def / (that: Currency) = this.amount.toDouble / that.amount
    val l = List(1,2,3)
  }
}
