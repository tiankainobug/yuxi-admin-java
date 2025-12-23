package com.yuxi.admin.service;

import com.yuxi.admin.entity.Dept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author tiankai
 * @since 2025-12-22
 */
public interface IDeptService extends IService<Dept> {

    List<Dept> getChildByParentId(List<Dept> depts, Long parentId);

}
