package com.xzx.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录视图
 * 作者: xzx
 * 创建时间: 2021-03-19-09-45
 **/
@Data
@ApiModel(value = "用户登录视图", description = "")
public class UserLoginVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "用户密码")
    private String password;
}
