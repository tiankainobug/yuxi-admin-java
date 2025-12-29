package com.yuxi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuxi.admin.entity.SysRole;
import com.yuxi.admin.entity.SysUser;
import com.yuxi.admin.mapper.UserMapper;
import com.yuxi.admin.service.ISysRoleService;
import com.yuxi.admin.service.UserService;
import com.yuxi.admin.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ISysRoleService sysRoleService;

    @Override
    public Map<String, Object> getUserInfo(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            // 根据token获取用户名
            String token = authorization.substring(7);
            String username = jwtUtil.getUsernameFromToken(token);

            LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();
            qw.eq(SysUser::getUserAccount, username);
            SysUser user = userMapper.selectOne(qw);

            // 获取用户基本信息
            HashMap<String, Object> resMap = new HashMap<>();
            resMap.put("id", user.getId());
            resMap.put("name", user.getUserName());
            resMap.put("avatar", user.getAvatarUrl());

            // 获取用户角色信息 code
            List<SysRole> roleList = sysRoleService.getRoleListByUserId(user.getId());
            resMap.put("roles", roleList.stream().map(SysRole::getRoleCode).collect(Collectors.toList()));

            // 获取用户权限信息 TODO


            return resMap;

        } else {
            return Collections.emptyMap();
        }
    }

    /**
     * 根据请求头获取用户列表
     * 如果用户信息携带了部门 id，则返回部门下的用户列表
     * @param sysUser： 用户信息
     * @param request： 请求对象
     * @return
     */
    @Override
    public List<SysUser> getUserListByRequest(SysUser sysUser, HttpServletRequest request) {
        Long deptId;
        deptId = sysUser.getDeptId();

        if (deptId == null) {
            // 没传部门 id，则返回该用户的部门 id
            String username = jwtUtil.getUsernameFromRequest(request);
            SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserAccount, username));
            deptId = user.getDeptId();
        }

        // 根据部门 id，获取部门下的人员列表
        return userMapper.selectList(new LambdaQueryWrapper<SysUser>().eq(SysUser::getDeptId, deptId));
    }
}