package com.xzx.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色视图
 * 作者: xzx
 * 创建时间: 2021-03-18-22-13
 **/
@Data
@ApiModel(value = "角色视图", description = "")
public class RoleVo implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "菜单id列表")
    List<Integer> menuIds;
    @ApiModelProperty(value = "接口id列表")
    List<Integer> apiIds;
    @ApiModelProperty(value = "角色主键id")
    private Integer roleId;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "角色昵称")
    private String nickName;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
