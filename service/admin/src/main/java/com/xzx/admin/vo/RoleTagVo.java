package com.xzx.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分配角色列表视图
 * 作者: xzx
 * 创建时间: 2021-03-18-23-03
 **/
@Data
@ApiModel(value = "分配角色列表视图", description = "用户分配角色时显示所有的角色列表")
public class RoleTagVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色主键id")
    private Integer roleId;
    @ApiModelProperty(value = "角色昵称")
    private String nickName;
}
