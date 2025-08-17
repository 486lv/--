package org.example.service.Impl;

import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.service.UserService;
import org.example.utils.Md5Util;
import org.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper usermapper;

    @Override
    public User findByUserName(String username) {
        User u=usermapper.findByUserName(username);
        return u;
    }

    @Override
    public void register(String username,String password,String email) {
        //加密
        if (email == null) {
            email=""; // 设置默认值为空字符串
        }
        String pwd_password= Md5Util.getMD5String(password);
        usermapper.add(username,pwd_password,email);
    }

    @Override
    public void updateUser(String username, String email) {
        Map<String,Object> map=ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        LocalDateTime time=LocalDateTime.now();
        usermapper.updateUser(id,username,email,time);
    }

    @Override
    public boolean noExistName(String username) {
        return usermapper.findByUserName(username)==null;
    }

    public void updatePwd(String newPwd) {
        Map<String,Object> map=ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        LocalDateTime time=LocalDateTime.now();
        usermapper.updatePwd(Md5Util.getMD5String(newPwd),id,time);
    }
}
