package com.yuxi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuxi.admin.entity.SysRole;
import com.yuxi.admin.entity.SysRoleMenu;
import com.yuxi.admin.entity.SysUserRole;
import com.yuxi.admin.mapper.SysRoleMapper;
import com.yuxi.admin.mapper.SysUserRoleMapper;
import com.yuxi.admin.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author tiankai
 * @since 2025-12-24
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public List<SysRole> getRoleListByUserId(Long userId) {
        LambdaQueryWrapper<SysUserRole> qw = new LambdaQueryWrapper<>();
        qw.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(qw);

        if (sysUserRoles != null && !sysUserRoles.isEmpty()) {
            List<Long> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            LambdaQueryWrapper<SysRole> qw1 = new LambdaQueryWrapper<>();
            qw1.in(SysRole::getId, roleIds);
            return this.list(qw1);
        }

        return Collections.emptyList();
    }
}
