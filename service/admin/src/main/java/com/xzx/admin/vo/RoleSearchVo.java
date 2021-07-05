package com.xzx.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色查询条件视图
 * 作者: xzx
 * 创建时间: 2021-03-19-23-26
 **/
@Data
@ApiModel(value = "角色查询条件视图", description = "用于模糊搜索")
public class RoleSearchVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "角色昵称")
    private String nickName;
    @ApiModelProperty(value = "查询开始时间", example = "2021-01-01 00:00:00")
    private String beginTime;
    @ApiModelProperty(value = "查询结束时间", example = "2021-01-01 00:00:00")
    private String endTime;
}
