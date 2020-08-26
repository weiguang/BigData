package com.okayjam.bigdata.hadoop;

import junit.framework.TestCase;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

/**
 * @author Chen weiguang chen2621978@gmail.com
 * @date 2020/08/26 21:16
 **/
public class FileUtilTest extends TestCase {
  /*链接获取方式：
    1.在命令行输入 hdfs getconf -confKey fs.default.name
    2.在core-site.xml中fs.defaultFS字段定义
    */

    public static final String HDFS_PATH = "hdfs://9.135.90.195:9000";
    public FileUtil fileUtil = new FileUtil(HDFS_PATH);

    /**
     *  初始
     * @throws Exception
     */
    public void setUp() throws Exception {
        System.out.println("start");
         FileSystem.get(URI.create(HDFS_PATH), new Configuration());
    }

    @Test
    public void  testmkdirs () throws IOException {
        fileUtil.mkdirs("/javaTest/te");
    }

    @Test
    public void  testRename () throws IOException {
        fileUtil.rename("/javaTest/te", "/javaTest/aa");
    }

    @Test
    public void  testLs () throws IOException {
        fileUtil.ls("/");
    }

        @Test
    public void  testDelete () throws IOException {
        fileUtil.deleteOnExit("/javaTest");
    }

    @Test
    public void  testDownload () throws IOException {
        fileUtil.download("/1.txt", ".");
    }


}