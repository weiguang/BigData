
## windows run configure

### 1. 需要下载hadoop 环境
Download url：
https://objects.githubusercontent.com/github-production-release-asset-2e65be/42450016/07bab578-8cd6-11e7-8682-592ce34f6978?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A%2F20221124%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20221124T024036Z&X-Amz-Expires=300&X-Amz-Signature=8c8cb1657849517bbb48e751272a4e1a5d51fee252465dfd3f79e367a295097c&X-Amz-SignedHeaders=host&actor_id=0&key_id=0&repo_id=42450016&response-content-disposition=attachment%3B%20filename%3Dhadoop-2.8.1.zip&response-content-type=application%2Foctet-stream

### 2. 下载解压之后，这是 HADOOP_HOME 和设置path为解压目录


查看运行情况：http://localhost:4040

 http://9.135.90.x:8080/

## 打包运行
maven 打包

上传文件到docker

```docker cp spark-1.0-SNAPSHOT.jar  aeedee98bdd9:/root/``` 

提交任务
`````/usr/local/spark-2.2.1/bin/spark-submit --name JamWordCountScala --class com.okayjam.bigdata.spark.stream.KafkaWordCount --master spark://master:7077 --executor-memory 1G --total-executor-cores 1 /home/spark-1.0-SNAPSHOT.jar ```