package com.yuxi.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxi.admin.common.Result;
import com.yuxi.admin.entity.SysMenu;
import com.yuxi.admin.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author tiankai
 * @since 2025-12-16
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController {

    @Autowired
    private ISysMenuService sysMenuService;


    /**
     * 获取菜单列表
     * @return 菜单列表
     */
    @GetMapping()
    public List<SysMenu> getMenuList() {
        // 获取所有的顶级菜单
        // 使用 LambdaQueryWrapper 替代 QueryWrapper，通过 SysMenu::getParentId 方法引用，提高类型安全性
        LambdaQueryWrapper<SysMenu> qw = new LambdaQueryWrapper<>();
        qw.isNull(SysMenu::getParentId);
        List<SysMenu> list = sysMenuService.list(qw);

        for (SysMenu menu : list) {
            List<SysMenu> sysMenus = sysMenuService.setMenuChildrenTree(menu);
            menu.setChildren(sysMenus);
        }

        return list;
    }

    /**
     * 添加菜单
     * @param menu 菜单
     * @return 添加结果
     */
    @PostMapping("/add")
    public Result addMenu(SysMenu menu) {
        boolean result = sysMenuService.save(menu);
        if (result) {
            return Result.success("添加成功");
        }
        return Result.failed("添加失败");
    }

    /**
     * 修改菜单
     * @param menu 菜单
     * @return 修改结果
     */
    @PostMapping("/update")
    public Result updateMenu(SysMenu menu) {
        boolean result = sysMenuService.updateById(menu);
        if (result) {
            return Result.success("修改成功");
        }
        return Result.failed("修改失败");
    }

    /**
     * 删除菜单
     * @param id 菜单id
     * @return 删除结果
     */
    @PostMapping("/delete")
    public Result deleteMenu(Long id) {
        // 判断是否有子菜单，有子菜单不让删除
        SysMenu byId = sysMenuService.getById(id);
        List<SysMenu> sysMenus = sysMenuService.setMenuChildrenTree(byId);
        if (sysMenus != null && sysMenus.size() > 0) {
            return Result.failed("请先删除子菜单！");
        }

        boolean result = sysMenuService.removeById(id);
        if (result) {
            return Result.success("删除成功");
        }
        return Result.failed("删除失败");
    }

}
