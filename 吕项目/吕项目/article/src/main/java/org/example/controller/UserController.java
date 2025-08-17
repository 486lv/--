package org.example.controller;

import jakarta.validation.constraints.Pattern;
import org.example.entity.Result;
import org.example.entity.User;
import org.example.service.UserService;
import org.example.utils.JwtUtil;
import org.example.utils.Md5Util;
import org.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")

public class UserController {
    @Autowired
    private UserService userservice;

    @PostMapping("/register")
    public Result register(@Pattern(regexp="^\\S{5,16}$")String username, @Pattern(regexp="^\\S{5,16}$")String password, String email){
        //查询用户,判断用户是否存在
        User u=userservice.findByUserName(username);
        //注册
        if(u==null) {
            userservice.register(username,password,email);
            return Result.success();
        }else{
            return Result.error("用户已存在");
        }
    }

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp="^\\S{5,16}$")String username, @Pattern(regexp="^\\S{5,16}$")String password){
        //查询用户
        User loginuser=userservice.findByUserName(username);
        //判断用户是否存在
        if(loginuser==null){
            return Result.error("用户名不存在");
        }
        //判断密码是否正确
        if(Md5Util.getMD5String(password).equals(loginuser.getPassword())) {
            Map<String,Object> claims=new HashMap<>();
            claims.put("id",loginuser.getId());
            claims.put("username",loginuser.getUsername());
            String token= JwtUtil.genToken(claims);
//
//            //token放入redis
//            ValueOperations<String,String> operations=stringRedisTemplate.opsForValue();
//            operations.set(token,token,1, TimeUnit.HOURS);

            return Result.success(token);
        }
        return Result.error("密码错误");
    }
    //用户详细信息
    @GetMapping("/userInfo")
    public Result<User> userInfo(){
        Map<String,Object> map= ThreadLocalUtil.get();
        String username=(String)map.get("username");
        User user = userservice.findByUserName(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result updateUser(@RequestBody User user) {
        String username = user.getUsername();
        String email = user.getEmail();
        userservice.updateUser(username, email);
        return Result.success();
    }


    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> params,@RequestHeader("Authorization") String token){
        //校验参数
        String oldPwd = params.get("old_pwd");//旧密码
        String newPwd = params.get("new_pwd");//新密码
        String rePwd = params.get("re_pwd");//确认密码
        if(!StringUtils.hasLength(oldPwd)||!StringUtils.hasLength(newPwd)||!StringUtils.hasLength(rePwd)){
            return Result.error("参数不能为空");
        }
        //原密码是否则正确
        Map<String,Object> map=ThreadLocalUtil.get();
        String username=(String)map.get("username");
        User loginUser=userservice.findByUserName(username);
        if(!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("原密码错误");
        }

        //新密码是否一致
        if(!newPwd.equals(rePwd)){
            return Result.error("新密码不一致");
        }

        userservice.updatePwd(newPwd);
//        //删除redis中的token
//        ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
//        operations.getOperations().delete(token);


        return Result.success();
    }


}
