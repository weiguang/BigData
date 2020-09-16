package com.okayjam.bigdata.flink.stream;

import com.okayjam.bigdata.flink.common.LineSplitter;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * you need run `nc -lk hostname port` before run this demo
 * @author Chen weiguang chen2621978@gmail.com
 * @date 2020/08/28 10:27
 **/
public class SocketWindowWordCount {

    static String  hostname = "localhost";
    static Integer  port = 44444;

    public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    // 通过连接 socket 获取输入数据
    DataStreamSource<String> stream = env.socketTextStream(hostname, port);

    //计数
    SingleOutputStreamOperator<Tuple2<String, Integer>> sum = stream.flatMap(new LineSplitter())
           // .keyBy(0)
            .keyBy((value)->value.getField(0))
            .timeWindow(Time.seconds(5), Time.seconds(3))
            .sum(1);

        // 将结果打印到控制台，注意这里使用的是单线程打印，而非多线程
        //sum.print().setParallelism(1);
        // sink 输出
        sum.addSink(new PrintSinkFunction<>());

        env.execute("Java WordCount from SocketTextStream Example");
    }


}



