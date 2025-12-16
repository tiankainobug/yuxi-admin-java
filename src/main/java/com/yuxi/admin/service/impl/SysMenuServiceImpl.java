package com.yuxi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxi.admin.entity.SysMenu;
import com.yuxi.admin.mapper.SysMenuMapper;
import com.yuxi.admin.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author tiankai
 * @since 2025-12-16
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    /**
     * 设置菜单子菜单树
     *
     * @param menu 菜单
     * @return 菜单树
     */
    @Override
    public List<SysMenu> setMenuChildrenTree(SysMenu menu) {

        // 获取子菜单
        List<SysMenu> list = this.list(new QueryWrapper<SysMenu>().eq("parent_id", menu.getId()));
        // 递归设置子菜单树
        if (list != null && list.size() > 0) {
            for (SysMenu sysMenu : list) {
                sysMenu.setChildren(this.setMenuChildrenTree(sysMenu));
            }
            return list;
        }

        return list;
    }
}
