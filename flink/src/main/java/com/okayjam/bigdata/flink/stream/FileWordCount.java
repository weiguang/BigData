package com.okayjam.bigdata.flink.stream;

import com.okayjam.bigdata.flink.common.LineSplitter;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * you need run `nc -lk hostname port` before run this demo
 * @author Chen weiguang chen2621978@gmail.com
 * @date 2020/08/28 10:27
 **/
public class FileWordCount {

    static String  hostname = "localhost";
    static Integer  port = 44444;

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置并行度为1
        env.setParallelism(1);

        //用txt文件作为数据源
        DataStream<String> textDataStream = env.readTextFile("file:///Users/jam/IdeaProjects/BigData/test/input/input.txt", "UTF-8");

        //统计单词数量并打印出来
        textDataStream
                .flatMap(new LineSplitter())
                .keyBy(value -> value.getField(0))
                .sum(1)
                .print();

        env.execute("API DataSource demo : readTextFile");
    }
}



