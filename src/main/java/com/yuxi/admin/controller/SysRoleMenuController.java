package com.yuxi.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuxi.admin.common.Result;
import com.yuxi.admin.entity.SysRoleMenu;
import com.yuxi.admin.service.ISysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色菜单关系表 前端控制器
 * </p>
 *
 * @author tiankai
 * @since 2025-12-24
 */
@RestController
@RequestMapping("/roleMenu")
public class SysRoleMenuController {

    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    @PostMapping("/list")
    public Result list(@RequestBody SysRoleMenu sysRoleMenu) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId, sysRoleMenu.getRoleId());
        List<SysRoleMenu> list = sysRoleMenuService.list(queryWrapper);

        ArrayList<Long> menuIds = new ArrayList<>();

        for (SysRoleMenu roleMenu : list) {
            menuIds.add(roleMenu.getMenuId());
        }

        return Result.success(menuIds);
    }

    @PostMapping("/update")
    public Result update(@RequestBody SysRoleMenu sysRoleMenu) {
        // 删除之前所有的
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId, sysRoleMenu.getRoleId());
        sysRoleMenuService.remove(queryWrapper);

        List<Long> menuIds = sysRoleMenu.getMenuIds();
        for (Long menuId : menuIds) {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(sysRoleMenu.getRoleId());
            roleMenu.setMenuId(menuId);
            sysRoleMenuService.save(roleMenu);
        }

        return Result.success();
    }

}
