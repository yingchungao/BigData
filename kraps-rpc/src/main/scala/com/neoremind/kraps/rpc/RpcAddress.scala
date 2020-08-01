package com.neoremind.kraps.rpc

import com.neoremind.kraps.util.Utils

/**
  * liuming
  * 2020/4/2 14:47 
  */
case class RpcAddress(host: String, port: Int) {
  def hostPort: String = host + ":" + port
  def toSparkURL: String = "spark://" + hostPort
  override def toString: String = hostPort
}

object RpcAddress {
  def fromURIString(uri: String): RpcAddress = {
    val uriObj = new java.net.URI(uri)
    RpcAddress(uriObj.getHost, uriObj.getPort)
  }
  def fromSparkURL(sparkUrl: String): RpcAddress = {
    val (host, port) = Utils.extractHostPortFromKrapsUrl(sparkUrl)
    RpcAddress(host, port)
  }
}
