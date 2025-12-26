package com.yuxi.admin.service;

import com.yuxi.admin.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author tiankai
 * @since 2025-12-24
 */
public interface ISysRoleService extends IService<SysRole> {

    List<SysRole> getRoleListByUserId(Long userId);
}
