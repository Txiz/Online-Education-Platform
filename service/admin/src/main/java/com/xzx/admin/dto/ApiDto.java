package com.xzx.admin.dto;

import com.xzx.admin.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 接口传输对象
 * 作者: xzx
 * 创建时间: 2021-03-18-23-41
 **/
@Data
@ApiModel(value = "接口传输对象", description = "")
public class ApiDto implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "角色列表")
    List<Role> roles;
    @ApiModelProperty(value = "接口主键id")
    private Integer apiId;
    @ApiModelProperty(value = "接口路径")
    private String apiUrl;
    @ApiModelProperty(value = "需要授权 0：不需要  1：需要")
    private Boolean isAuth;
}

