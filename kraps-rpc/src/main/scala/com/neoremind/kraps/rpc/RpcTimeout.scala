package com.neoremind.kraps.rpc

import java.util.concurrent.TimeoutException

import com.neoremind.kraps.util.Utils
import com.neoremind.kraps.{RpcConf, RpcException}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.control.NonFatal

/**
  * liuming
  * 2020/4/2 15:26 
  */
class RpcTimeoutException(message: String, cause: TimeoutException)
  extends TimeoutException(message) {
  initCause(cause)
}

class RpcTimeout(val duration: FiniteDuration, val timeoutProp: String)
  extends Serializable {

  private def createRpcTimeoutException(te: TimeoutException): RpcTimeoutException = {
    new RpcTimeoutException(te.getMessage + ". This timeout is controlled by " + timeoutProp, te)
  }
  def addMessageIfTimeout[T]: PartialFunction[Throwable, T] = {
    case rte: RpcTimeoutException => throw rte
    case te: TimeoutException => throw createRpcTimeoutException(te)
  }
  def awaitResult[T](future: Future[T]): T = {
    val wrapAndRethrow: PartialFunction[Throwable, T] = {
      case NonFatal(t) => throw new RpcException("Exception thrown in awaitResult", t)
    }
    try {
      Await.result(future, duration)
    } catch addMessageIfTimeout.orElse(wrapAndRethrow)
  }
}


object RpcTimeout {
  def apply(conf: RpcConf, timeoutProp: String): RpcTimeout = {
    val timeout = {
      conf.getTimeAsSeconds(timeoutProp).seconds
    }
    new RpcTimeout(timeout, timeoutProp)
  }
  def apply(conf: RpcConf, timeoutProp: String, defaultValue: String): RpcTimeout = {
    val timeout = {
      conf.getTimeAsSeconds(timeoutProp, defaultValue).seconds
    }
    new RpcTimeout(timeout, timeoutProp)
  }
  def apply(conf: RpcConf, timeoutPropList: Seq[String], defaultValue: String): RpcTimeout = {
    require(timeoutPropList.nonEmpty)
    val itr = timeoutPropList.iterator
    var foundProp: Option[(String, String)] = None
    while (itr.hasNext && foundProp.isEmpty) {
      val propKey = itr.next()
      conf.getOption(propKey).foreach(prop => foundProp = Some(propKey, prop))
    }
    val finalProp = foundProp.getOrElse(timeoutPropList.head, defaultValue)
    val timeout = {
      Utils.timeStringAsSeconds(finalProp._2).seconds
    }
    new RpcTimeout(timeout, finalProp._1)
  }
}