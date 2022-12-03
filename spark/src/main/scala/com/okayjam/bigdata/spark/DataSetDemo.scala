package com.okayjam.bigdata.spark

import org.apache.spark.sql.{SaveMode, SparkSession}

object DataSetDemo {
    def main(args: Array[String]): Unit = {
      val spark = SparkSession.builder().master("local")
        .appName("nextjson")
        .getOrCreate()
      //读取数据文件
      val df = spark.read.format("json").load("spark/src/main/resources/employees.json")
      df.printSchema()
      df//.toDF("name", "age")
      df.write.option("header","true").mode(SaveMode.Append).csv("./spark/1.txt")
    }
}
