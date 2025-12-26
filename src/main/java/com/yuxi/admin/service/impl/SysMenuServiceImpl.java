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
import java.util.stream.Collectors;

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

    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        ArrayList<SysMenu> tree = new ArrayList<>();

        // 遍历找到最顶层菜单,然后添加子菜单
        for (SysMenu menu : menus) {
            Long parentId = menu.getParentId();
            // 该菜单的父亲菜单是否在列表中存在，不存在的话，为根节点
            List<SysMenu> parentCollect = menus.stream().filter(m -> m.getId().equals(parentId)).collect(Collectors.toList());
            if (parentCollect.size() == 0) {
                // 对跟节点，添加子菜单
                menu.setChildren(this.getChildByParentId(menus, menu.getId()));
                tree.add(menu);
            }
        }
        return tree;
    }
}
