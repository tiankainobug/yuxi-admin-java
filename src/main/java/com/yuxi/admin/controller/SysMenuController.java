package com.yuxi.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxi.admin.common.Result;
import com.yuxi.admin.entity.SysMenu;
import com.yuxi.admin.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public Result getMenuList(@RequestParam(value = "name", required = false) String name) {
        // 使用 LambdaQueryWrapper 替代 QueryWrapper，通过 SysMenu::getParentId 方法引用，提高类型安全性
        LambdaQueryWrapper<SysMenu> qw = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            qw.like(SysMenu::getName, name);
        }

        List<SysMenu> list = sysMenuService.list(qw);

        ArrayList<SysMenu> tree = new ArrayList<>();

        // 遍历找到最顶层菜单,然后添加子菜单
        for (SysMenu menu : list) {
            Long parentId = menu.getParentId();
            List<SysMenu> parentCollect = list.stream().filter(m -> m.getId().equals(parentId)).collect(Collectors.toList());
            if (parentCollect.size() == 0) {
                menu.setChildren(sysMenuService.getChildByParentId(list, menu.getId()));
                tree.add(menu);
            }
        }

        return Result.success(tree);
    }

    /**
     * 添加菜单
     * @param menu 菜单
     * @return 添加结果
     */
    @PostMapping("/add")
    public Result addMenu(@RequestBody SysMenu menu) {
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
    public Result updateMenu(@RequestBody SysMenu menu) {
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
    @PostMapping("/delete/{id}")
    public Result deleteMenu(@PathVariable Long id) {
        // 判断是否有子菜单，有子菜单不让删除
        SysMenu byId = sysMenuService.getById(id);

        LambdaQueryWrapper<SysMenu> qw = new LambdaQueryWrapper<>();
        qw.eq(SysMenu::getParentId, id);
        List<SysMenu> sysMenus = sysMenuService.list(qw);

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
