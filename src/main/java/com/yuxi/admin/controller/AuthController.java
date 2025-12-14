package com.yuxi.admin.controller;

import com.yuxi.admin.entity.Role;
import com.yuxi.admin.entity.SysUserRole;
import com.yuxi.admin.mapper.SysUserRoleMapper;
import com.yuxi.admin.service.RoleService;
import com.yuxi.admin.utils.JwtUtil;
import com.yuxi.admin.entity.User;
import com.yuxi.admin.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Api(tags = "认证管理")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Map<String, Object> login(
            @ApiParam("用户名") @RequestParam String username,
            @ApiParam("密码") @RequestParam String password) {

        Map<String, Object> result = new HashMap<>();

        try {
            // 获取明文密码
            // 使用Spring Security验证用户凭据
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException e) {
            result.put("success", false);
            result.put("message", "用户名或密码错误");
            return result;
        }

        // 加载用户详细信息
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 生成JWT令牌
        final String token = jwtUtil.generateToken(userDetails.getUsername());

        result.put("success", true);
        result.put("message", "登录成功");
        result.put("token", token);
        result.put("username", username);

        return result;
    }

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Map<String, Object> register(
            @ApiParam("用户名") @RequestParam String username,
            @ApiParam("密码") @RequestParam String password,
            @ApiParam("姓名") @RequestParam String name,
            @ApiParam("角色") @RequestParam Long roleId
    ) {

        Map<String, Object> result = new HashMap<>();

        // 检查用户是否已存在
        User existingUser = userService.getOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>()
                .eq("user_account", username));

        if (existingUser != null) {
            result.put("success", false);
            result.put("message", "用户名已存在");
            return result;
        }

        // 创建新用户
        User newUser = new User();
        newUser.setUserAccount(username);
        newUser.setUserName(name);

        // 插入角色
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(newUser.getId());
        sysUserRole.setRoleId(roleId);
        sysUserRoleMapper.insert(sysUserRole);

        newUser.setUserPassword(passwordEncoder.encode(password)); // 密码加密

        boolean saved = userService.save(newUser);

        if (saved) {
            result.put("success", true);
            result.put("message", "注册成功");
        } else {
            result.put("success", false);
            result.put("message", "注册失败");
        }

        return result;
    }
}