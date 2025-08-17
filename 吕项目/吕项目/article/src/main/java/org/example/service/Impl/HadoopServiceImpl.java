package org.example.service.Impl;

import org.apache.hadoop.conf.Configuration;
import org.example.mapper.ArticleMapper;
import org.example.service.HadoopService;
import org.example.utils.HadoopUtils;
import org.example.wordcount.WordCountDriver_v2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class HadoopServiceImpl implements HadoopService {
    @Value("${hadoop.hadooppath}")
    String hadoop;
    @Value("${hadoop.rootfile}")
    String root;


    @Autowired
    private HadoopUtils hadoopUtils;
    @Override
    public void wordcount(String[] args) throws Exception {
        WordCountDriver_v2 worldCountDriver = new WordCountDriver_v2();
        worldCountDriver.run(args);
    }

    @Override
    public int dowork() throws Exception {
//        Article endarticle=articleMapper.findlastfile();
        String hadoopfilepath = "/article";
//        hadoopfilepath+=endarticle.getTitle();
//        hadoopfilepath+=".txt";
        Random rand = new Random();
        String hadoopresult = "/result/";

        int x = rand.nextInt(1000000000 + 1);
        System.out.println(hadoopfilepath);
        hadoopresult += x;

        System.out.println(hadoopresult);
        String[] args=new String[2];
        args[0]=hadoopfilepath;
        args[1]=hadoopresult;
        wordcount(args);
        return x;
    }


    public Map<String, Integer> show(int x) throws Exception {
        //下载
        String hadoopresult = "/result/";
        String rootpath = "D:/result/";
        hadoopresult += x;
        hadoopresult+="/part-r-00000";
        rootpath += x;
        rootpath += ".txt";

        Configuration conf = new Configuration();
        System.setProperty("HADOOP_USER_NAME","root");

        hadoopUtils.downloadFromHadoop(rootpath,hadoopresult);

        //文件传送
        Map<String, Integer> result = readFileToMap(rootpath);
        return result;
    }

    public Map<String, Integer> readFileToMap(String filePath) {
        Map<String, Integer> map = new HashMap<>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                // 按制表符分割每一行
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    String key = parts[0];
                    int value = Integer.parseInt(parts[1]);
                    map.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
