package com.yuxi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxi.admin.entity.SysMenu;
import com.yuxi.admin.mapper.SysMenuMapper;
import com.yuxi.admin.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * @param menus 菜单列表 parentId 上级 id
     * @return 菜单树
     */
    @Override
    public List<SysMenu> getChildByParentId(List<SysMenu> menus, Long parentId) {
        ArrayList<SysMenu> tree = new ArrayList<>();

        for (SysMenu menu : menus) {
            if (parentId.equals(menu.getParentId())) {
                menu.setChildren(getChildByParentId(menus, menu.getId()));
                tree.add(menu);
            }
        }

        return tree;
    }
}
