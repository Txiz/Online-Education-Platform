package com.xzx.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 密码视图
 * 作者: xzx
 * 创建时间: 2021-03-18-21-51
 **/
@Data
@ApiModel(value = "密码视图", description = "")
public class PasswordVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "旧密码")
    private String op;
    @ApiModelProperty(value = "新密码")
    private String np;
    @ApiModelProperty(value = "确认密码")
    private String cp;
}
