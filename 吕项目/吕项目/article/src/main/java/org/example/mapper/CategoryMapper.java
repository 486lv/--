package org.example.mapper;

import org.example.entity.Category;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Mapper
public interface CategoryMapper {
    //新增
    @Insert("INSERT INTO category (category_name) " +
            "VALUES (#{categoryName})")
    void add(Category category);

    //查询所有
    @Select("SELECT id, category_name FROM category")
    List<Category> list();
    //更新
    @Update("update category set category_name=#{categoryName} where id=#{id}")
    void update(Category category);

    //根据id删除
    @Delete("delete from category where id=#{id}")
    void deleteById(Integer id);
}
