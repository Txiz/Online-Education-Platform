package com.xzx.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口查询条件视图
 * 作者: xzx
 * 创建时间: 2021-03-19-23-54
 **/
@Data
@ApiModel(value = "接口查询条件视图", description = "用于模糊搜索")
public class ApiSearchVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "接口名称")
    private String apiName;
    @ApiModelProperty(value = "接口路径")
    private String apiUrl;
    @ApiModelProperty(value = "需要授权 0：不需要  1：需要")
    private Boolean isAuth;
    @ApiModelProperty(value = "查询开始时间", example = "2021-01-01 00:00:00")
    private String beginTime;
    @ApiModelProperty(value = "查询结束时间", example = "2021-01-01 00:00:00")
    private String endTime;
}
