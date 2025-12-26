package com.yuxi.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuxi.admin.common.Result;
import com.yuxi.admin.entity.SysMenu;
import com.yuxi.admin.entity.SysRole;
import com.yuxi.admin.entity.SysRoleMenu;
import com.yuxi.admin.entity.SysUser;
import com.yuxi.admin.mapper.SysMenuMapper;
import com.yuxi.admin.service.ISysMenuService;
import com.yuxi.admin.service.ISysRoleMenuService;
import com.yuxi.admin.service.ISysRoleService;
import com.yuxi.admin.service.UserService;
import com.yuxi.admin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private ISysMenuService sysMenuService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private UserService userService;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/list")
    public Result list(HttpServletRequest  request) {
        String authorization = request.getHeader("Authorization");
        // 根据token获取用户名
        String token = authorization.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        // 管理员返回所有菜单
        if (username.equals("admin")) {
            // 获取所有菜单
            List<SysMenu> list = sysMenuService.list();
            // 构建菜单数
            List<SysMenu> tree = sysMenuService.buildMenuTree(list);
            return Result.success(tree);
        }
        // 其他用户根据角色获取菜单
        // 获取用户
        LambdaQueryWrapper<SysUser> userQW = new LambdaQueryWrapper<>();
        userQW.eq(SysUser::getUserAccount, username);
        SysUser user = userService.getOne(userQW);
        // 根据用户 id 获取菜单
        List<SysMenu> menus = sysMenuMapper.selectMenusByUserId(user.getId());
        // 构建菜单数
        List<SysMenu> tree = sysMenuService.buildMenuTree(menus);

        return Result.success(tree);
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
