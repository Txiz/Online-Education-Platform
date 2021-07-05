package com.xzx.education.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程查询视图
 * 作者: xzx
 * 创建时间: 2021-03-20-22-38
 **/
@Data
@ApiModel(value = "课程查询视图", description = "")
public class CourseSearchVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程名称")
    private String courseName;
    @ApiModelProperty(value = "课程描述")
    private String description;
    @ApiModelProperty(value = "查询开始时间", example = "2021-01-01 00:00:00")
    private String beginTime;
    @ApiModelProperty(value = "查询结束时间", example = "2021-01-01 00:00:00")
    private String endTime;
}
