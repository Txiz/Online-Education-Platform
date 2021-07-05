package com.xzx.education.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户视图
 * 作者: xzx
 * 创建时间: 2021-03-19-13-56
 **/

@Data
@ApiModel(value = "用户视图", description = "")
public class UserVo implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "角色id列表")
    List<Integer> roleIds;
    @ApiModelProperty(value = "用户主键id")
    private Integer userId;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "用户密码")
    private String password;
    @ApiModelProperty(value = "用户简介")
    private String description;
    @ApiModelProperty(value = "用户头像")
    private String avatar;
    @ApiModelProperty(value = "是否启用 0：未启用    1:已启用")
    private Boolean isEnable;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
