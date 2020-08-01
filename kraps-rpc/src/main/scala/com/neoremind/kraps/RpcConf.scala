package com.neoremind.kraps

import java.util.concurrent.ConcurrentHashMap
import com.neoremind.kraps.util.Utils
import scala.collection.JavaConverters._

/**
  * liuming
  * 2020/4/2 14:16 
  */
case class RpcConf(loadDefaults: Boolean = true) extends Cloneable with Serializable {
  def this() = this(true)
  private val settings = new ConcurrentHashMap[String, String]()
  loadFromSystemProperties(false)
  def loadFromSystemProperties(slient: Boolean): RpcConf = {
    for ((key, value) <- Utils.getSystemProperties if  key.startsWith("spark.")) {
      set(key, value)
    }
    this
  }
  def set(key: String, value: String): RpcConf = {
    settings.put(key, value)
    this
  }
  def remove(key: String): RpcConf = {
    settings.remove(key)
    this
  }
  def get(key: String): String = {
    getOption(key).getOrElse(throw new NoSuchElementException(key))
  }
  def get(key: String, defaultValue: String): String = {
    getOption(key).getOrElse(defaultValue)
  }
  def getOption(key: String): Option[String] = {
    Option(settings.get(key))
  }
  def getAll: Array[(String, String)] = {
    settings.entrySet().asScala.map(x => (x.getKey, x.getValue)).toArray
  }
  def getAllWithPrefix(prefix: String): Array[(String, String)] = {
    getAll.filter {
      case (k, v) => k.startsWith(prefix)
    }.map {
      case (k, v) => (k.substring(prefix.length), v)
    }
  }
  def getTimeAsSeconds(key: String): Long = {
    Utils.timeStringAsSeconds(get(key))
  }
  def getTimeAsSeconds(key: String, defaultValue: String): Long = {
    Utils.timeStringAsSeconds(get(key, defaultValue))
  }
  def getTimeAsMs(key: String): Long = {
    Utils.timeStringAsMs(get(key))
  }
  def getTimeAsMs(key: String, defaultValue: String): Long = {
    Utils.timeStringAsMs(get(key, defaultValue))
  }
  def getSizeAsBytes(key: String): Long = {
    Utils.byteStringAsBytes(get(key))
  }
  def getSizeAsBytes(key: String, defaultValue: String): Long = {
    Utils.byteStringAsBytes(get(key, defaultValue))
  }
}
