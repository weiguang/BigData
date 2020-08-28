package com.okayjam.bigdata.hadoop.mapreduce;

import com.okayjam.bigdata.hadoop.partition.MyPartition;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @author Chen weiguang chen2621978@gmail.com
 * @date 2020/08/27 12:49
 **/
public class WordCount {
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();

        // 本地运行
       // conf.set("mapreduce.framework.name", "local");

        //在hadoop集群机器上运行会读取相应的配置文件 $Hadoop_Home
        Job job = Job.getInstance(conf);

        job.setJarByClass(WordCount.class);//上传Jar


        //map & reduce 过程设置
        job.setMapperClass(WordCountMapper.class);//mapper 过程
        job.setReducerClass(WordCountReducer.class);//reduce 过程

        job.setMapOutputKeyClass(Text.class);//mapper 输出key
        job.setMapOutputValueClass(IntWritable.class);//mapper 输出value

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);


        //文件路径设置
/*        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));*/
        // 本地运行输入、输出路径
        FileInputFormat.setInputPaths(job, new Path("/Users/jam/IdeaProjects/BigData/hadoop/test/input"));
        FileOutputFormat.setOutputPath(job, new Path("/Users/jam/IdeaProjects/BigData/hadoop/test/output"));

        // reduce 个数， 分区个数
        job.setNumReduceTasks(5);

        //自定义分区方法
        job.setPartitionerClass(MyPartition.class);

        job.setCombinerClass(WordCountReducer.class);


        boolean flag = job.waitForCompletion(true);

        System.out.println("the job exe is :" + flag);
    }

}
