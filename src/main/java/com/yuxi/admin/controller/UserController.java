package com.yuxi.admin.controller;

import com.yuxi.admin.entity.User;
import com.yuxi.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取所有用户
     * @return 用户列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation("获取所有用户")
    public List<User> getUserList() {
        return userService.list();
    }

    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.username == #id.toString()")
    @ApiOperation("根据ID获取用户")
    public User getUserById(@ApiParam("用户ID") @PathVariable Long id) {
        return userService.getById(id);
    }

    /**
     * 创建用户
     * @param user 用户信息
     * @return 是否成功
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("创建用户")
    public boolean createUser(@ApiParam("用户信息") @RequestBody User user) {
        return userService.save(user);
    }

    /**
     * 更新用户
     * @param user 用户信息
     * @return 是否成功
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or principal.username == #user.id.toString()")
    @ApiOperation("更新用户")
    public boolean updateUser(@ApiParam("用户信息") @RequestBody User user) {
        return userService.updateById(user);
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否成功
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("删除用户")
    public boolean deleteUser(@ApiParam("用户ID") @PathVariable Long id) {
        return userService.removeById(id);
    }
}