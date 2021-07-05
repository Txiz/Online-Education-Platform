package com.xzx.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户查询条件视图
 * 作者: xzx
 * 创建时间: 2021-03-19-23-09
 **/
@Data
@ApiModel(value = "用户查询条件视图", description = "用于模糊搜索")
public class UserSearchVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "用户简介")
    private String description;
    @ApiModelProperty(value = "是否启用 0：未启用    1:已启用")
    private Boolean isEnable;
    @ApiModelProperty(value = "查询开始时间", example = "2021-01-01 00:00:00")
    private String beginTime;
    @ApiModelProperty(value = "查询结束时间", example = "2021-01-01 00:00:00")
    private String endTime;
}
