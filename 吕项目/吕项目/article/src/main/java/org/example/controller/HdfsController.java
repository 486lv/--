package org.example.controller;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.example.wordcount.WordCountDriver_v2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;

@RestController
@RequestMapping("/hdfs")
public class HdfsController {

    /**
     * 将本地文件srcFile,上传到hdfs
     * @param srcFile
     * @return
     */
    @RequestMapping("/mkdir")
    public void uploadDir(@RequestParam String srcFile) throws IOException {
        Configuration conf=new Configuration();

        //System.setProperty("HADOOP_USER_NAME","root");
        conf.set("fs.defaultFS","hdfs://192.168.86.128:8020");
        FileSystem client=FileSystem.get(conf);
        client.mkdirs(new Path(srcFile));
        client.close();
    }
    @RequestMapping("/upload")
    public void uploadFile(@RequestParam String rootpath,@RequestParam String hadooppath) throws IOException {
        Configuration conf=new Configuration();
        //System.setProperty("HADOOP_USER_NAME","root");
        conf.set("fs.defaultFS","hdfs://192.168.86.128:8020");
        FileSystem client=FileSystem.get(conf);

        InputStream inputStream=new FileInputStream(rootpath);
        OutputStream outputStream=client.create(new Path(hadooppath));

        IOUtils.copyBytes(inputStream,outputStream,1024);
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        client.close();
    }



    @RequestMapping("/download")
    public void download(@RequestParam String hadooppath,@RequestParam String rootpath)  throws IOException{
        Configuration conf=new Configuration();
        //System.setProperty("HADOOP_USER_NAME","root");
        conf.set("fs.defaultFS","hdfs://192.168.86.128:8020");
        FileSystem client=FileSystem.get(conf);
        InputStream inputStream=client.open(new Path(hadooppath));
        OutputStream outputStream=new FileOutputStream(rootpath);
        IOUtils.copyBytes(inputStream,outputStream,1024);
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        client.close();
    }
    @RequestMapping("/delFile")
    public void del(@RequestParam String hadooppath) throws IOException{
        Configuration conf=new Configuration();
        //System.setProperty("HADOOP_USER_NAME","root");
        conf.set("fs.defaultFS", "hdfs://192.168.86.128:8020");
        FileSystem client=FileSystem.get(conf);
        client.delete(new Path(hadooppath),true);
        client.close();
    }
    @RequestMapping("/do")
    public void dowork(@RequestParam String hadooppath,@RequestParam String rootpath) throws Exception {
        String[] str=new String[2];
        str[0]=hadooppath;
        str[1]=rootpath;
        WordCountDriver_v2 worldCountDriver = new WordCountDriver_v2();
        worldCountDriver.run(str);
    }
}
