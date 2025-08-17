package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.entity.User;
import org.springframework.transaction.jta.UserTransactionAdapter;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    User findByUserName(String username);

    @Insert("insert into user(username, password,email,time) values(#{username}, #{password},#{email},now())")
    void add(String username, String password,String email);


    @Update("UPDATE user SET username = #{username}, email = #{email} ,time=#{time} WHERE id = #{id}")
    void updateUser(Integer id, String username, String email, LocalDateTime time);


    @Update("update user set password=#{md5String},time=#{time} where id=#{id}")
    void updatePwd(String md5String, Integer id,LocalDateTime time);
}