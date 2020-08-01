package com.neoremind.kraps

/**
  * liuming
  * 2020/4/2 14:44 
  */
class RpcException(message: String, cause: Throwable) extends Exception(message, cause){

  def this(message: String) = this(message, null)

}
