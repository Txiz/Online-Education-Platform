package com.xzx.education.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 课程视图
 * 作者: xzx
 * 创建时间: 2021-03-20-22-03
 **/
@Data
@ApiModel(value = "课程视图", description = "")
public class CourseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程主键id")
    private Integer courseId;
    @ApiModelProperty(value = "课程名称")
    private String courseName;
    @ApiModelProperty(value = "课程封面")
    private String courseCover;
    @ApiModelProperty(value = "课程描述")
    private String description;
    @ApiModelProperty(value = "课程分类id")
    private Integer categoryId;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "课程分类名称")
    private String categoryName;
    @ApiModelProperty(value = "任课教师id列表")
    private List<Integer> teacherIds;
    @ApiModelProperty(value = "任课教师名称列表")
    private List<String> teacherNames;
}
