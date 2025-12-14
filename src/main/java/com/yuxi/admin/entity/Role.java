package com.yuxi.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 功能：
 * 作者：田凯
 * 日期：2025/12/14 22:54
 **/
@TableName("sys_role")
@Data
public class Role {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
    * 状态
    * */
    private String status;

    /**
    * 创建时间
    * */
    private String createTime;

    /**
    * 修改时间
    * */
    private String updateTime;
}