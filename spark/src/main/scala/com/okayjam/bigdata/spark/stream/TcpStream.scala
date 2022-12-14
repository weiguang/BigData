package com.okayjam.bigdata.spark.stream

/**
 *
 *
 * @author Chen weiguang chen2621978@gmail.com 
 * @date 2022/12/13 10:30
 * */

import org.apache.spark._
import org.apache.spark.streaming._


/**
 * spark 作为客户端连接到server，并且接收信息
 *
 * linux 可以使用nc命令  nc -lk 9999
 * 下面提供python的简单测试代码，启动python，执行启动tcp server，如果程序连接就会发送I am jamchen, 然后启动spark就会收到消息并统计单词
 * import socketserver
 * HOST, PORT = "localhost", 9999
 * server = socketserver.TCPServer((HOST, PORT), lambda request, client_address,self: {request.sendall(str.encode('I am jamchen'))})
 * server.serve_forever()
 */

object TcpStream {
  def main(args:Array[String]) {
    // 可以设置hdfs用户，避免权限问题
    //    System.setProperty("HADOOP_USER_NAME", "spark")
    // 创建一个local StreamingContext，包含2个工作线程，并将批次间隔设为5秒
    // master至少需要2个CPU核，以避免出现任务饿死的情况
    val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
    val ssc = new StreamingContext(conf, Seconds(1))
    // 减少LOG输出
    ssc.sparkContext.setLogLevel("WARN")

    // 创建一个连接到hostname:port的DStream，如：localhost:9999
    val lines = ssc.socketTextStream("localhost", 9999)


    // 将每一行分割成多个单词
    val words = lines.flatMap(_.split(" "))

    // 对每一批次中的单词进行计数
    val pairs = words.map(word => (word, 1))
    val wordCounts = pairs.reduceByKey(_ + _)
    // 带滑动窗口归并
//    val wordCounts = pairs.reduceByKeyAndWindow((x:Int,y : Int) =>x+y, Seconds(20), Seconds(5))
    // 将该DStream产生的RDD的头十个元素打印到控制台上
    wordCounts.print()
    ssc.start()            // 启动流式计算
    ssc.awaitTermination()  // 等待直到计算终止

  }

}
