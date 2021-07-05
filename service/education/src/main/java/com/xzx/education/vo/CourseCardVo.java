package com.xzx.education.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 课程卡片视图
 * 作者: xzx
 * 创建时间: 2021-04-09-17-36
 **/
@Data
@ApiModel(value = "课程卡片视图", description = "")
public class CourseCardVo implements Serializable {

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
    @ApiModelProperty(value = "任课教师名称列表")
    private List<String> teacherNames;
    @ApiModelProperty(value = "学习人数")
    private Integer studentNum;
    @ApiModelProperty(value = "评论数")
    private Integer commentNum;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
