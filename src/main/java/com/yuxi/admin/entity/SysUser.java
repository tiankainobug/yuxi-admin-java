package com.yuxi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 项目管理系统用户表
 * </p>
 *
 * @author tiankai
 * @since 2025-12-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value="SysUser对象", description="项目管理系统用户表")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户唯一主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "登录账号（唯一）")
    private String userAccount;

    @ApiModelProperty(value = "加密后的密码（BCrypt/SHA256）")
    private String userPassword;

    @ApiModelProperty(value = "用户真实姓名")
    private String userName;

    @ApiModelProperty(value = "手机号码（可选，脱敏存储）")
    private String userPhone;

    @ApiModelProperty(value = "所属部门ID（关联部门表）")
    private Long deptId;

    @ApiModelProperty(value = "用户状态：0-禁用，1-正常，3-锁定")
    private Integer userStatus;

    @ApiModelProperty(value = "头像URL")
    private String avatarUrl;

    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "最后登录IP")
    private String lastLoginIp;

    @ApiModelProperty(value = "创建人ID（关联本表id）")
    private Long creatorId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人ID（关联本表id）")
    private Long updaterId;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "逻辑删除：0-未删除，1-已删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "备注（如入职时间/岗位说明）")
    private String remark;

    @ApiModelProperty(value = "用户角色关系")
    @TableField(exist = false)
    private List<SysRole> roleList;


}
