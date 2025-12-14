package com.yuxi.admin.security;

import com.yuxi.admin.entity.Role;
import com.yuxi.admin.entity.User;
import com.yuxi.admin.mapper.RoleMapper;
import com.yuxi.admin.service.RoleService;
import com.yuxi.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查找用户
        User user = userService.getOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>()
                .eq("user_account", username));

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // 2. 查询用户绑定的所有角色编码（核心：多角色）
        List<Role> roleList = roleService.getRoleListByUserId(user.getId());
        user.setRoleList(roleList); // 赋值给用户

        return new org.springframework.security.core.userdetails.User(
                user.getUserAccount(),
                user.getUserPassword(),
                new ArrayList<>() // 在实际应用中应该设置用户的权限列表
        );
    }
}