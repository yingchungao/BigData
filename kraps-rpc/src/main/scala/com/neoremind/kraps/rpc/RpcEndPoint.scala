package com.neoremind.kraps.rpc

import com.neoremind.kraps.RpcException

/**
  * liuming
  * 2020/4/2 14:57 
  */

trait RpcEnvFactory {
  def create(config: RpcEnvConfig): RpcEnv
}
trait RpcEndPoint {

  val rpcEnv: RpcEnv
  final def self: RpcEndpointRef = {
    require(rpcEnv != null, "rpcEnv has not been initialized")
    rpcEnv.endpointRef(this)
  }

  def receive: PartialFunction[Any, Unit] ={
    case _ => throw new RpcException(self + "does not implements 'receive'")
  }
  def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
    case _ => context.sendFailure(new RpcException(self + " wont reply anything"))
  }
  def onError(cause: Throwable): Unit = {
    throw cause
  }
  def onConnected(remoteAddress: RpcAddress): Unit = {

  }
  def onDisconnected(remoteAddress: RpcAddress): Unit ={

  }
  def onNetwordError(cause: Throwable, remote: RpcAddress): Unit = {

  }
  def onStart(): Unit{

  }
  def onStop(): Unit = {

  }
  final def stop() {
    val _self = self
    if (_self != null) {
      rpcEnv.stop(_self)
    }
  }
}

trait ThreadSafeRpcEndpoint extends RpcEndPoint
