package org.example.service;


import org.example.entity.User;

public interface UserService {
    User findByUserName(String username);
    void register(String username, String password,String email);

    void updateUser(String username,String email);

    boolean noExistName(String username);

    void updatePwd(String newPwd);

//    void update(User user);
}
