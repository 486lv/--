package org.example.demo;

import org.apache.commons.math3.analysis.function.Exp;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.apache.hadoop.fs.FileSystem;
import org.mockito.internal.util.io.IOUtil;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import com.jcraft.jsch.*;

//public class HDFSMkDir {

//    @Test
//    public void doing() throws IOException{
//        doingssh("/test1","/result/file");
//    }


//    public void doingssh(String hadooppath,String rootpath)  throws IOException{
//            String host = "192.168.86.128";
//            String user = "root";
//            String password = "362514"; // 如果需要密码
//            String cmd = "hadoop jar wordcount-1.0-SNAPSHOT.jar";  // 要执行的命令
//        cmd += " ";
//        cmd += hadooppath;
//        cmd += " ";
//        cmd += rootpath;
//            executeSSHCommand(host, user, password, cmd);
//        }
//
//        public static void executeSSHCommand(String host, String user, String password, String command) {
//            JSch jsch = new JSch();
//            try {
//                Session session = jsch.getSession(user, host);
//                session.setPassword(password);
//                session.setConfig("StrictHostKeyChecking", "no");
//
//                // 连接到远程主机
//                session.connect();
//
//                // 创建一个 ChannelExec 用于执行命令
//                ChannelExec channel = (ChannelExec) session.openChannel("exec");
//                channel.setCommand(command);
//
//                // 获取命令的输出
//                channel.setInputStream(null);
//                channel.setErrStream(System.err);
//
//                // 执行命令
//                channel.connect();
//
//                // 读取输出
//                InputStream inputStream = channel.getInputStream();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    System.out.println(line);
//                }
//
//                channel.disconnect();
//            } catch (JSchException | IOException e) {
//                e.printStackTrace();
//            }
//        }

//    @Test
//    public void dowork( String hadooppath,String rootpath ) throws IOException {
//       exeu("hadoop jar wordcount-1.0-SNAPSHOT.jar /test1 /result/file111");
//    }
//       public void exeu(String command) throws IOException{
//            ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", command);
//            Process process = processBuilder.start();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//            try {
//                int exitCode = process.waitFor();
//                System.out.println(exitCode);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
////    @Value("${hadoop.name-node}")
////    private String hadoopNameNode;
////
////
////    public void test(String hadoopNameSpace) throws Exception{
////
////        Configuration conf=new Configuration();
////        //System.setProperty("HADOOP_USER_NAME","root");
////        conf.set("fs.defaultFS","hdfs://192.168.86.128:8020");
////        FileSystem client=FileSystem.get(conf);
////        System.out.println(hadoopNameSpace);
////        client.mkdirs(new Path(hadoopNameSpace));
////        client.close();
////
////    }
////    @Test
////    public void sdaf() throws Exception{
////        test("/test3");
////    }
////
//////    @Test
//////    public void test1() throws Exception{
//////        Configuration conf=new Configuration();
//////        //System.setProperty("HADOOP_USER_NAME","root");
//////        conf.set("fs.defaultFS","hdfs://192.168.86.128:8020");
//////        FileSystem client=FileSystem.get(conf);
//////
//////        InputStream inputStream=new FileInputStream("D:/files/safsdfsafsf.txt");
//////        OutputStream outputStream=client.create(new Path("/test1/file01"));
//////
//////        IOUtils.copyBytes(inputStream,outputStream,1024);
//////        outputStream.flush();
//////        outputStream.close();
//////        inputStream.close();
//////        client.close();
//////    }
//////    @Test
//////    public void Test2() throws Exception{
//////        Configuration conf=new Configuration();
//////        //System.setProperty("HADOOP_USER_NAME","root");
//////        conf.set("fs.defaultFS","hdfs://192.168.86.128:8020");
//////        FileSystem client=FileSystem.get(conf);
//////
//////        InputStream inputStream=client.open(new Path("/test1/file01"));
//////        OutputStream outputStream=new FileOutputStream("D:/files/1.txt");
//////        IOUtils.copyBytes(inputStream,outputStream,1024);
//////        outputStream.flush();
//////        outputStream.close();
//////        inputStream.close();
//////        client.close();
//////    }
//////    @Test
//////    public void Test3() throws Exception{
//////        Configuration conf=new Configuration();
//////        //System.setProperty("HADOOP_USER_NAME","root");
//////        conf.set("fs.defaultFS","hdfs://192.168.86.128:8020");
//////        FileSystem client=FileSystem.get(conf);
//////
//////        client.delete(new Path("/test1/file01"),true);
//////
//////        client.close();
//////
//
//}
