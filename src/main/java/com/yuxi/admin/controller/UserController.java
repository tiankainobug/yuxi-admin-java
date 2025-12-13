package com.yuxi.admin.controller;

import com.yuxi.admin.entity.User;
import com.yuxi.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取所有用户
     * @return 用户列表
     */
    @GetMapping("/list")
    public List<User> getUserList() {
        return userService.list();
    }

    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

    /**
     * 创建用户
     * @param user 用户信息
     * @return 是否成功
     */
    @PostMapping("/create")
    public boolean createUser(@RequestBody User user) {
        return userService.save(user);
    }

    /**
     * 更新用户
     * @param user 用户信息
     * @return 是否成功
     */
    @PutMapping("/update")
    public boolean updateUser(@RequestBody User user) {
        return userService.updateById(user);
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否成功
     */
    @DeleteMapping("/delete/{id}")
    public boolean deleteUser(@PathVariable Long id) {
        return userService.removeById(id);
    }
}