package com.yuxi.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuxi.admin.common.Result;
import com.yuxi.admin.entity.Dept;
import com.yuxi.admin.entity.SysMenu;
import com.yuxi.admin.service.IDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author tiankai
 * @since 2025-12-22
 */
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private IDeptService deptService;

    @PostMapping("/list")
    public Result list(@RequestBody Dept dept) {
        LambdaQueryWrapper<Dept> qw = new LambdaQueryWrapper<>();

        if (dept.getDeptName() != null && !dept.getDeptName().isEmpty()) {
            qw.like(Dept::getDeptName, dept.getDeptName());
        }
        if (dept.getStatus() != null) {
            qw.eq(Dept::getStatus, dept.getStatus());
        }

        List<Dept> list = deptService.list(qw);

        ArrayList<Dept> tree = new ArrayList<>();

        // 遍历找到最顶层菜单,然后添加子菜单
        for (Dept dept1 : list) {
            Long parentId = dept1.getParentDeptId();
            // 该菜单的父亲菜单是否在列表中存在，不存在的话，为根节点
            List<Dept> parentCollect = list.stream().filter(m -> m.getDeptId().equals(parentId)).collect(Collectors.toList());
            if (parentCollect.size() == 0) {
                // 对跟节点，添加子菜单
                dept1.setChildren(deptService.getChildByParentId(list, dept1.getDeptId()));
                tree.add(dept1);
            }
        }

        return Result.success(tree);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Dept dept) {
        boolean save = deptService.save(dept);
        return save ? Result.success() : Result.failed();
    }

    @PostMapping("/update")
    public Result update(@RequestBody Dept dept) {
        boolean update = deptService.updateById(dept);
        return update ? Result.success() : Result.failed();
    }

    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        // 查看是否有下级部门
        LambdaQueryWrapper<Dept> qw = new LambdaQueryWrapper<>();
        qw.eq(Dept::getParentDeptId, id);
        if (deptService.count(qw) > 0) {
            return Result.failed("请先删除该部门下的子部门");
        }

        boolean delete = deptService.removeById(id);
        return delete ? Result.success() : Result.failed();
    }

}
