package com.neoremind.kraps.rpc

/**
  * liuming
  * 2020/4/2 14:53 
  */
trait RpcCallContext {

  def reply(response: Any): Unit
  def sendFailure(e: Throwable): Unit
  def senderAddress: RpcAddress

}
