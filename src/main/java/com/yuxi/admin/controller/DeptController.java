package com.yuxi.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuxi.admin.common.Result;
import com.yuxi.admin.entity.Dept;
import com.yuxi.admin.service.IDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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




        return null;
    }

}
