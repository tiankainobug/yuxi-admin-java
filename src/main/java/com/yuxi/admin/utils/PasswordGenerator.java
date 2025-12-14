package com.yuxi.admin.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// 测试代码生成加密密码
public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPwd = encoder.encode("123456");
        System.out.println(encryptedPwd); // 输出类似：$2a$10$e8V1z4c0Y8L7G9b5H6F3D2S1A9B8N7M6K5J4H3G2F1D0S9A8B7C6D5E4F3G2H1
    }
}