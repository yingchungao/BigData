package com.neoremind.kraps.rpc

import com.neoremind.kraps.{RpcConf, RpcException}
import com.neoremind.kraps.util.RpcUtils
import org.slf4j.LoggerFactory

import scala.concurrent.Future
import scala.reflect.ClassTag

/**
  * liuming
  * 2020/4/2 15:11 
  */
abstract class RpcEndpointRef(conf: RpcConf) extends Serializable {
  private val log = LoggerFactory.getLogger(classOf[RpcEndpointRef])

  private[this] val maxRetries = RpcUtils.numRetries(conf)
  private[this] val retryWaitMs = RpcUtils.retryWaitMs(conf)
  private[this] val defaultAskTimeout = RpcUtils.askRpcTimeout(conf)

  def address: RpcAddress

  def name: String

  def send(message: Any): Unit

  def ask[T: ClassTag](message: Any, timeout:RpcTimeout):Future[T]

  def askWithRetry[T: ClassTag](message: Any): T = askWithRetry(message, defaultAskTimeout)

  def askWithRetry[T: ClassTag](message: Any, timeout: RpcTimeout): T = {
    var attempts = 0
    var lastException: Exception = null
    while (attempts < maxRetries) {
      attempts += 1
      try {
        val future = ask[T](message, timeout)
        val result = timeout.awaitResult(future)
        if (result == null) {
          throw new RpcException("RpcEndPoint returned null")
        }
        return result
      } catch {
        case ie: InterruptedException => throw ie
        case e: Exception =>
          lastException = e
          log.warn(s"Error sending message [message = $message] in $attempts attempts",e)
      }

      if (attempts < maxRetries) {
        Thread.sleep(retryWaitMs)
      }

      throw new RpcException(
        s"Error sending message [message = $message]", lastException
      )

    }
  }

}
