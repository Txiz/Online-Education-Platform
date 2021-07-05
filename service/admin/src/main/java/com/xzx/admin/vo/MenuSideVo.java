package com.xzx.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单栏视图
 * 作者: xzx
 * 创建时间: 2021-03-19-08-43
 **/
@Data
@ApiModel(value = "菜单栏视图", description = "动态菜单侧边栏")
public class MenuSideVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单主键id")
    private Integer menuId;
    @ApiModelProperty(value = "菜单名称")
    private String menuName;
    @ApiModelProperty(value = "菜单路由（指前端路由路径）")
    private String path;
    @ApiModelProperty(value = "菜单图标")
    private String icon;
    @ApiModelProperty(value = "父菜单id")
    private Integer parentId;
    @ApiModelProperty(value = "是否隐藏 0：不隐藏    1:隐藏")
    private Boolean isHidden;
    @ApiModelProperty(value = "菜单排序")
    private Integer sort;
    @ApiModelProperty(value = "子菜单")
    private List<MenuSideVo> children;
}
