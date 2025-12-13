package com.yuxi.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yuxi.admin.mapper")
public class YuxiAdminJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(YuxiAdminJavaApplication.class, args);
    }

}
