package org.example.entity;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class User {
    private int id;              // 自增ID
    private String username;     // 用户名
    private String password;     // 密码
    private String email;        // 邮箱
    private LocalDateTime time;  // 时间
}
