package com.xzx.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 作者: xzx
 * 创建时间: 2021-04-02-11-12
 **/
@Data
@ApiModel(value = "User对象", description = "")
public class PeopleRegisterVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "教师或学生的用户名	")
    private String peopleName;
    @ApiModelProperty(value = "教师或学生的密码")
    private String password;
    @ApiModelProperty(value = "教师或学生的姓名")
    private String nickName;
    @ApiModelProperty(value = "教师或学生的头像")
    private String avatar;
    @ApiModelProperty(value = "教师还是学生  0：学生  1：教师")
    private Boolean isWho;
}
