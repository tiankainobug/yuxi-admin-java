package com.yuxi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuxi.admin.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {

    List<Role> getRoleListByUserId(Long userId);
}
