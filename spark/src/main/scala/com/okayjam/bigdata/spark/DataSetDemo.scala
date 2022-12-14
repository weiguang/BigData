package com.okayjam.bigdata.spark

/**
 *
 *
 * @author Chen weiguang chen2621978@gmail.com 
 * @date 2022/12/14 11:08
 * */
import org.apache.spark.sql.{SaveMode, SparkSession}

object DataSetDemo {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .master("local[2]")
      .appName("reader1")
      .getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
    val textFile =  spark.read.textFile("spark/src/main/resources/words.txt")
    import spark.implicits._
    val words= textFile.flatMap(x=>x.split(" "))

    // 将RDD[String] 转为 DataFrame
    val wordsDataFrame = words.toDF("word")
    wordsDataFrame.printSchema()

    // 创建view
    wordsDataFrame.createOrReplaceTempView("words")

    val sqlContext = SparkSession.builder.getOrCreate()
    // 再用SQL语句查询，并打印出来
    val wordCountsDataFrame =
      sqlContext.sql("select word, count(*) as total from words group by word")
    wordCountsDataFrame.show()

    // save result to file
    wordCountsDataFrame.repartition(1).write.option("header","true").mode(SaveMode.Append).csv("./spark/temp/save")

    spark.stop()
  }
}


