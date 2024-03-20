package com.okayjam.bigdata.spark

/**
 * @author ${user.name}
 */
object App1 {

  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)

  def main(args : Array[String]) {
    println( "Hello World!" )
    println("concat arguments = " + foo(args))
  }

}

