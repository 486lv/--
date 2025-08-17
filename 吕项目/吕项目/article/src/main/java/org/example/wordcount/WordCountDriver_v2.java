package org.example.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCountDriver_v2 {
    public void run(String[] args)throws Exception
    {
//        System.setProperty("hadoop.home.dir", "C:\\hadoop"); // 必须最早执行
        Configuration conf=new Configuration();

        System.setProperty("HADOOP_USER_NAME","root");

        conf.set("fs.defaultFS", "hdfs://mycluster/");
        conf.set("dfs.nameservices","mycluster");
        conf.set("dfs.ha.namenodes.mycluster","nn1,nn2");
        conf.set("dfs.namenode.rpc-address.mycluster.nn1","192.168.88.151:8020");
        conf.set("dfs.namenode.rpc-address.mycluster.nn2","192.168.88.152:8020");
        conf.set("dfs.client.failover.proxy.provider.mycluster","org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        Job job=Job.getInstance(conf,WordCountDriver_v2.class.getSimpleName());

        job.setJarByClass(WordCountDriver_v2.class);
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
        FileSystem fs=FileSystem.get(conf);
        if(fs.exists(new Path(args[1])))
        {
            fs.delete(new Path(args[1]),true);
        }
        boolean abc=job.waitForCompletion(true);
    }
}
