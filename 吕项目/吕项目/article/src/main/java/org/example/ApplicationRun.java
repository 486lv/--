package org.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableAsync
@MapperScan("org.example.mapper")
public class ApplicationRun
{
    public static void main( String[] args )
    {
        SpringApplication.run(ApplicationRun.class,args);
    }
}
