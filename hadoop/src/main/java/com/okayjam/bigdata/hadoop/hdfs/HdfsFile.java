package com.okayjam.bigdata.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 * @author Chen weiguang chen2621978@gmail.com
 * @date 2022/10/13 19:50
 **/
public class HdfsFile {
	public static void main(String[] args) throws IOException {
		//创建configuration对象
		Configuration conf = new Configuration();
		//创建FileSystem对象
		//需求：查看hdfs集群服务器/user/zyh/passwd.txt的内容
		FileSystem fs = FileSystem.get(URI.create("hdfs://9.135.90.195:9000/test/t.txt"), conf);
		// args[0] hdfs://1.0.0.3:9000/user/zyh/passwd.txt
		// args[0] file:///etc/passwd.txt
		FSDataInputStream is = fs.open(new Path("hdfs://9.135.90.195:9000/test/t.txt"));
		OutputStream os=new FileOutputStream(new File("D:/a.txt"));
		byte[] buff= new byte[1024];
		int length = 0;
		while ((length=is.read(buff))!=-1){
			System.out.println(new String(buff,0,length));
			os.write(buff,0,length);
			os.flush();
		}
		System.out.println(fs.getClass().getName());
		//这个是根据你传的变量来决定这个对象的实现类是哪个
	}

}
