package com.yuxi.admin.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author tiankai
 * @since 2025-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_menu")
@ApiModel(value="SysMenu对象", description="菜单表")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "父级菜单 id")
    private Long parentId;

    @ApiModelProperty(value = "菜单名字")
    private String name;

    @ApiModelProperty(value = "菜单类型 0-目录；1-菜单；2-按键；3-外链；")
    private Integer type;

    @ApiModelProperty(value = "路由名称")
    private String routerName;

    @ApiModelProperty(value = "路由路径")
    private String routerPath;

    @ApiModelProperty(value = "组件路径")
    private String component;

    @ApiModelProperty(value = "显示状态")
    private Integer visible;

    @ApiModelProperty(value = "缓存状态 0-关闭；1-开启")
    private Integer keepalive;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "权限标识")
    private String perm;

    @ApiModelProperty(value = "子菜单")
    @TableField(exist = false)
    private List<SysMenu> children;


}
