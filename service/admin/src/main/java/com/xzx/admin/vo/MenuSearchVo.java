package com.xzx.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜单查询条件视图
 * 作者: xzx
 * 创建时间: 2021-03-19-23-41
 **/
@Data
@ApiModel(value = "菜单查询条件视图", description = "用于模糊搜索")
public class MenuSearchVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;
    @ApiModelProperty(value = "是否隐藏 0：不隐藏    1:隐藏")
    private Boolean isHidden;
    @ApiModelProperty(value = "查询开始时间", example = "2021-01-01 00:00:00")
    private String beginTime;
    @ApiModelProperty(value = "查询结束时间", example = "2021-01-01 00:00:00")
    private String endTime;
}
