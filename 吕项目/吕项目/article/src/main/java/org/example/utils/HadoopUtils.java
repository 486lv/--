package org.example.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class HadoopUtils {

    @Value("${hadoop.hadooppath}")
    private String hadoopBasePath;

    @Value("${hadoop.rootfile}")
    private String localBasePath;

    // 获取 HDFS 客户端
    public FileSystem getFileSystem() throws Exception {
        System.setProperty("HADOOP_USER_NAME", "root");
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://mycluster/");
        conf.set("dfs.nameservices", "mycluster");
        conf.set("dfs.ha.namenodes.mycluster", "nn1,nn2");
        conf.set("dfs.namenode.rpc-address.mycluster.nn1", "192.168.88.151:8020");
        conf.set("dfs.namenode.rpc-address.mycluster.nn2", "192.168.88.152:8020");
        conf.set("dfs.client.failover.proxy.provider.mycluster", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        return FileSystem.get(conf);
    }

    // 上传本地已有文件到 HDFS（去重：已存在则先删除）
    public void uploadFileToHadoop(String localPath, String hadoopPath) throws Exception {
        FileSystem fs = getFileSystem();
        Path hdfsPath = new Path(hadoopPath);
        try {
            if (fs.exists(hdfsPath)) {
                fs.delete(hdfsPath, true);
            }
            try (InputStream in = new FileInputStream(localPath);
                 OutputStream out = fs.create(hdfsPath, true)) {
                IOUtils.copyBytes(in, out, 1024);
            }
        } finally {
            fs.close();
        }
    }

    // 先保存内容到本地文件，再上传到 HDFS（去重）
    public void uploadArticleToHadoop(String content, String localPath, String hadoopPath) throws Exception {
        // 1. 保存内容到本地
        try (FileWriter writer = new FileWriter(localPath)) {
            writer.write(content);
        }

        // 2. 上传到 HDFS（去重）
        FileSystem fs = getFileSystem();
        Path hdfsPath = new Path(hadoopPath);
        try {
            if (fs.exists(hdfsPath)) {
                fs.delete(hdfsPath, true);
            }
            try (InputStream in = new FileInputStream(localPath);
                 OutputStream out = fs.create(hdfsPath, true)) {
                IOUtils.copyBytes(in, out, 1024);
            }
        } finally {
            fs.close();
        }
    }


    // 从 HDFS 下载文件到本地
    public void downloadFromHadoop(String localPath,String hadoopPath) throws Exception {
        FileSystem fs = getFileSystem();
        try (InputStream in = fs.open(new Path(hadoopPath));
             OutputStream out = new FileOutputStream(localPath)) {
            IOUtils.copyBytes(in, out, 1024);
        }
        fs.close();
    }

    // 删除 HDFS 文件和本地文件
    public void deleteFromHadoop(String localPath,String hadoopPath) throws Exception {
        FileSystem fs = getFileSystem();
        try {
            fs.delete(new Path(hadoopPath), true);
        } finally {
            fs.close();
        }
        java.io.File file = new java.io.File(localPath);
        if (file.exists()) {
            file.delete();
        }
    }
}