package com.yuxi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuxi.admin.entity.SysUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface UserService extends IService<SysUser> {

    Map<String, Object> getUserInfo(HttpServletRequest request);

}