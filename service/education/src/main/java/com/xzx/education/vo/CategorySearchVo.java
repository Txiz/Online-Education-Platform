package com.xzx.education.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分类查询条件视图
 * 作者: xzx
 * 创建时间: 2021-03-20-15-49
 **/
@Data
@ApiModel(value = "分类查询条件视图", description = "")
public class CategorySearchVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程分类名称")
    private String categoryName;
    @ApiModelProperty(value = "查询开始时间", example = "2021-01-01 00:00:00")
    private String beginTime;
    @ApiModelProperty(value = "查询结束时间", example = "2021-01-01 00:00:00")
    private String endTime;
}
