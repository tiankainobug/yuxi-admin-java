package com.yuxi.admin.service;

import com.yuxi.admin.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author tiankai
 * @since 2025-12-16
 */
public interface ISysMenuService extends IService<SysMenu> {

    List<SysMenu> buildMenuTree(List<SysMenu> menus, Long parentId);

}
