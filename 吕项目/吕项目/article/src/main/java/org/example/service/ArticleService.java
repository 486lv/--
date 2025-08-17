package org.example.service;

import org.apache.hadoop.fs.FileSystem;
import org.example.entity.Article;
import org.example.entity.PageBean;

import java.io.IOException;
import java.util.Map;

public interface ArticleService {
    String[] path(Article article);

    //新增文章
    void add(Article article) throws Exception;

    //条件分页列表查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);

    Article update(Article article);

    void deleteById(Integer id) throws Exception;

}
