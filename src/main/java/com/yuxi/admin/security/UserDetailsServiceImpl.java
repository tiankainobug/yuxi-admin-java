package com.yuxi.admin.security;

import com.yuxi.admin.entity.SysRole;
import com.yuxi.admin.entity.SysUser;
import com.yuxi.admin.service.ISysRoleService;
import com.yuxi.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private ISysRoleService sysRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查找用户
        SysUser user = userService.getOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SysUser>()
                .eq("user_account", username));

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // 2. 查询用户绑定的所有角色编码（核心：多角色）
        List<SysRole> roleList = sysRoleService.getRoleListByUserId(user.getId());
        user.setRoleList(roleList); // 赋值给用户

        System.out.println("用户角色列表：" + roleList);

        // 3. 核心修复：将角色编码转换为 GrantedAuthority 权限集合
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (SysRole role : roleList) {
            // 注意：hasAuthority 直接校验 roleCode 原值（如 ADMIN），无需加 ROLE_ 前缀
            authorities.add(new SimpleGrantedAuthority(role.getRoleCode()));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUserAccount(),
                user.getUserPassword(),
                authorities // 在实际应用中应该设置用户的权限列表
        );
    }
}