package com.okayjam.bigdata.spark.stream

/**
 * windows need download hadoop tool
 *
 * @author Chen weiguang chen2621978@gmail.com 
 * @date 2022/11/24 10:18
 * */

import org.apache.spark._
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.internal.Logging


object KafkaWordCount {
  def main(args:Array[String]){
    // 可以设置hdfs用户，避免权限问题
//    System.setProperty("HADOOP_USER_NAME", "spark")
    // 设置 2 cores
    val sparkConf = new SparkConf().setAppName("KafkaWordCount").setMaster("local[2]")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("WARN")
    //批次间隔设为10秒
    val ssc = new StreamingContext(sc,Seconds(10))
    //设置检查点，如果存放在HDFS上面，则写成类似ssc.checkpoint("/user/hadoop/checkpoint")这种形式，但是，要启动Hadoop
    ssc.checkpoint("./spark/kafka/checkpoint")
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "9.135.90.x:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "jam_spark_stream",
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (true: java.lang.Boolean)
    )
    val topics = Array("mykafka")
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )
    stream.foreachRDD(rdd => {
      val offsetRange = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      val maped: RDD[(String, String)] = rdd.map(record => (record.key,record.value))
      val lines = maped.map(_._2)
      val words = lines.flatMap(_.split(" "))
      val pair = words.map(x => (x,1))
      val wordCounts = pair.reduceByKey(_+_)
      wordCounts.foreach(println)
//      wordCounts.foreach(logger.info)

    })
    ssc.start
    ssc.awaitTermination
  }
}