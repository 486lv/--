package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.entity.Article;

import java.util.List;

@Mapper
public interface ArticleMapper {
    //新增
    @Insert("insert into article(title,content,category_id,create_user,create_time,update_time) " +
            "values(#{title},#{content},#{categoryId},#{createUser},#{createTime},#{updateTime})")
    void add(Article article);

    @Select("SELECT * FROM article")
    List<Article> list();

    @Update("update article set title=#{title},content=#{content},category_id=#{categoryId},update_time=#{updateTime} where id=#{id}")
    Article update(Article article);

    @Delete("delete from article where id=#{id}")
    void deleteById(Integer id);

    @Select("SELECT * FROM article")
    List<Article> findAll();

    @Select("select * FROM article where id=#{id}")
    Article findById(Integer id);


    @Select("SELECT COUNT(*)>0 FROM article WHERE title=#{filename}")
    Boolean fileExist(String filename);

//    @Select("SELECT * from ORDER by id desc limit 1")
//    Article findlastfile();

}
