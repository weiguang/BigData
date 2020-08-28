package com.okayjam.bigdata.hadoop.mapreduce;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author Chen weiguang chen2621978@gmail.com
 * @date 2020/08/27 12:43
 **/
public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int count = 0;
        for (IntWritable value : values) {
             count += value.get();
        }
        context.write(key, new IntWritable(count));
    }
}
