package com.yuxi.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.yuxi.admin.mapper")
@EnableSwagger2
public class YuxiAdminJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(YuxiAdminJavaApplication.class, args);
    }

}
