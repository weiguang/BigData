package com.okayjam.bigdata.flink.common;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @author Chen weiguang chen2621978@gmail.com
 * @date 2020/08/28 11:07
 **/
public class LineSplitter implements FlatMapFunction<String, Tuple2<String, Integer>> {
    @Override
    public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) {
        String[] tokens = s.toLowerCase().split("\\W+");

        for (String token: tokens) {
            if (token.length() > 0) {
                collector.collect(new Tuple2<String, Integer>(token, 1));
            }
        }
    }
}


