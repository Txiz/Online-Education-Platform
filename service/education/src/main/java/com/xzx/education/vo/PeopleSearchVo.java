package com.xzx.education.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 账号查询视图
 * 作者: xzx
 * 创建时间: 2021-03-31-13-10
 **/
@Data
@ApiModel(value = "People对象", description = "")
public class PeopleSearchVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "教师或学生的用户名	")
    private String peopleName;
    @ApiModelProperty(value = "教师或学生的姓名")
    private String nickName;
    @ApiModelProperty(value = "教师还是学生  0：学生  1：教师")
    private Boolean isWho;
    @ApiModelProperty(value = "查询开始时间", example = "2021-01-01 00:00:00")
    private String beginTime;
    @ApiModelProperty(value = "查询结束时间", example = "2021-01-01 00:00:00")
    private String endTime;
}
