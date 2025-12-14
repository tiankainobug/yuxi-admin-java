package com.yuxi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuxi.admin.entity.Role;
import com.yuxi.admin.mapper.RoleMapper;
import com.yuxi.admin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 功能：
 * 作者：田凯
 * 日期：2025/12/14 23:06
 **/
@Service
public class roleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRoleListByUserId(Long userId) {
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("id", userId);
        return roleMapper.selectList(roleQueryWrapper);
    }
}
