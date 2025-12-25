package com.yuxi.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuxi.admin.common.Result;
import com.yuxi.admin.entity.SysRole;
import com.yuxi.admin.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统角色表 前端控制器
 * </p>
 *
 * @author tiankai
 * @since 2025-12-24
 */
@RestController
@RequestMapping("/role")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    @PostMapping("/list")
    public Result list(@RequestBody SysRole sysRole) {
        LambdaQueryWrapper<SysRole> qw = new LambdaQueryWrapper<>();
        if (sysRole.getRoleName() != null && !sysRole.getRoleName().isEmpty()) {
            qw.like(SysRole::getRoleName, sysRole.getRoleName());
        }
        if (sysRole.getStatus() != null) {
            qw.eq(SysRole::getStatus, sysRole.getStatus());
        }
        if (sysRole.getRoleCode() != null && !sysRole.getRoleCode().isEmpty()) {
            qw.like(SysRole::getRoleCode, sysRole.getRoleCode());
        }
        return Result.success(sysRoleService.list(qw));
    }

    @PostMapping("/add")
    public Result add(@RequestBody SysRole sysRole) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleCode, sysRole.getRoleCode());
        if (sysRoleService.count(queryWrapper) > 0) {
            return Result.failed("角色编码已存在");
        }

        boolean save = sysRoleService.save(sysRole);
        return save ? Result.success() : Result.failed();
    }

    @PostMapping("/update")
    public Result update(@RequestBody SysRole sysRole) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleCode, sysRole.getRoleCode());
        queryWrapper.ne(SysRole::getId, sysRole.getId());
        if (sysRoleService.count(queryWrapper) > 0) {
            return Result.failed("角色编码已存在");
        }

        boolean update = sysRoleService.updateById(sysRole);
        return update ? Result.success() : Result.failed();
    }

    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        boolean delete = sysRoleService.removeById(id);
        return delete ? Result.success() : Result.failed();
    }


}
