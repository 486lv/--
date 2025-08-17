package org.example.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.hadoop.conf.Configuration;
import org.example.wordcount.WordCountDriver_v2;
import org.example.entity.Article;
import org.example.entity.PageBean;
import org.example.mapper.ArticleMapper;
import org.example.service.ArticleService;
import org.example.utils.ThreadLocalUtil;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.example.utils.HadoopUtils;
@Service
public class ArticleServiceImpl implements ArticleService {
    @Value("${hadoop.hadooppath}")
    String hadoop;
    @Value("${hadoop.rootfile}")
    String root;

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private HadoopUtils hadoopUtils;
    public String[] path(Article article){
        String hadoopFilePath = hadoop;
        hadoopFilePath += article.getTitle() + ".txt";
        // /files/aaa.txt
        String rootpath = root;
        rootpath = rootpath + article.getTitle() + ".txt";
        // D:/files/aaa.txt
        String[] paths=new String[2];
        paths[0]=hadoopFilePath;
        paths[1]=rootpath;
        return paths;
    }



    @Override
    public void add(Article article) throws Exception {
        //补充属性值
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        article.setCreateUser(userId);
        if (article.getCoverImg() == null || article.getCoverImg().isEmpty()) {
            article.setCoverImg("default_cover_img_url"); // 设置默认封面图像URL
        }
        if(articleMapper.fileExist(article.getTitle())){
            articleMapper.deleteById(article.getId());
        }

        articleMapper.add(article);

        String[] paths=path(article);
        String hadooppath=paths[0];
        String rootpath=paths[1];
// 将 HTML 内容转为纯文本再上传
        String plainText = Jsoup.parse(article.getContent()).text();
        hadoopUtils.uploadArticleToHadoop(plainText, rootpath, hadooppath);
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        //1.创建PageBean对象
        PageBean<Article> pb = new PageBean<>();

        //2.开启分页查询 PageHelper
        PageHelper.startPage(pageNum, pageSize);

        //3.调用mapper
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        List<Article> as = articleMapper.list();
        //Page中提供了方法,可以获取PageHelper分页查询后 得到的总记录条数和当前页数据
        Page<Article> p = (Page<Article>) as;

        //把数据填充到PageBean对象中
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }
    //这里没有使用
    @Override
    public Article update(Article article) {
        article.setUpdateTime(LocalDateTime.now());
        return articleMapper.update(article);
    }

    @Override
    public void deleteById(Integer id) throws Exception {
        Article article=articleMapper.findById(id);
        String[] paths=path(article);
        String hadooppath=paths[0];
        String rootpath=paths[1];

       hadoopUtils.deleteFromHadoop(rootpath,hadooppath);
       articleMapper.deleteById(id);
    }





}
