package com.okayjam.bigdata.hadoop.partition;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * @author Chen weiguang chen2621978@gmail.com
 * @date 2020/08/27 17:53
 **/
public class MyPartition extends Partitioner<Text, IntWritable> {
    @Override
    public int getPartition(Text text, IntWritable intWritable, int numPartitions) {
        String first = text.toString().substring(0, 1);
        if (first.charAt(0) >= 'a' && first.charAt(0) < 'h') return 0;
        else if (first.charAt(0) >= 'h' && first.charAt(0) < 'o') return 1;
        else if (first.charAt(0) >= 'o' && first.charAt(0) <= 'z') return 2;
        else return 4;
    }
}
