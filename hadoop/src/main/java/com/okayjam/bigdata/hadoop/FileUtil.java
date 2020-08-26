package com.okayjam.bigdata.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;

/**
 * @author Chen weiguang chen2621978@gmail.com
 * @date 2020/08/26 21:15
 **/
public class FileUtil {
    private  Configuration configuration = null;
    private  FileSystem fs = null;

    public  String HDFS_PATH  = null;

    public FileUtil(String hdfs_path) {
        HDFS_PATH = hdfs_path;
        configuration = getConfiguration();
        fs = getFileSystem();
    }

    public  Configuration getConfiguration() {
        if(null == configuration){
            synchronized (FileUtil.class){
                if(null == configuration) {
                    configuration = new Configuration();
                }
            }
        }
        return configuration;
    }

    public FileSystem getFileSystem() {
        if(null == fs){
            synchronized (FileUtil.class){
                if(null == fs) {
                    try {
                        fs = FileSystem.get(URI.create(HDFS_PATH), getConfiguration());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return fs;
    }

    /**
     * 创建文件
     * @param file
     * @param content
     * @throws IOException
     */
    public void createFile(String file, String content) throws IOException {
        byte[] buff = content.getBytes();
        FSDataOutputStream os = null;
        try {
            os = fs.create(new Path(file));
            os.write(buff, 0, buff.length);
            System.out.println("Create: " + file);
        } finally {
            if (os != null)
                os.close();
        }
//        fs.close();
    }

    /**
     * 创建目录
     * @throws IOException
     */
    public void mkdirs(String folder) throws IOException {
        Path path = new Path(folder);
        if (!fs.exists(path)) {
            fs.mkdirs(path);
            System.out.println("Create: " + folder);
        }
    }

    /**
     * 删除文件或目录
     * @param folder
     * @throws IOException
     */
    public void deleteOnExit(String folder) throws IOException {
        Path path = new Path(folder);
        fs.deleteOnExit(path);
//        fs.close();
        System.out.println("Delete: " + folder);
    }

    /**
     * 重命名文件
     * @param src
     * @param dst
     * @throws IOException
     */
    public void rename(String src, String dst) throws IOException {
        Path name1 = new Path(src);
        Path name2 = new Path(dst);
        fs.rename(name1, name2);
//        fs.close();
        System.out.println("Rename: from " + src + " to " + dst);
    }

    /**
     * 遍历文件
     * @param folder
     * @throws IOException
     */
    public void ls(String folder) throws IOException {
        Path path = new Path(folder);
        FileStatus[] list = fs.listStatus(path);
        System.out.println("ls: " + folder);
        System.out.println("=========================================");
        for (FileStatus f : list) {
            System.out.printf("name: %s, folder: %s, size: %d\n", f.getPath(), f.isDirectory(), f.getLen());
        }
        System.out.println("=========================================");
//        fs.close();
    }

    /**
     * 拷贝文件到HDFS
     * @param local
     * @param remote
     * @throws IOException
     */
    public void copyFile(String local, String remote) throws IOException {
        fs.copyFromLocalFile(new Path(local), new Path(remote));
        System.out.println("copy from: " + local + " to " + remote);
//        fs.close();
    }
    /**
     * 从HDFS中下载文件到本地中
     * @param remote
     * @param local
     * @throws IOException
     */
    public void download(String remote, String local) throws IOException {
        Path path = new Path(remote);
        fs.copyToLocalFile(false, path, new Path(local), true);
//        fs.copyToLocalFile( path, new Path(local));  // windows not work
        System.out.println("download: from" + remote + " to " + local);
//        fs.close();
    }


}
