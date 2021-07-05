package com.xzx.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author xzx
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_role_api")
@ApiModel(value = "RoleApi对象", description = "")
public class RoleApi implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "接口id")
    private Integer apiId;

    @ApiModelProperty(value = "角色id")
    private Integer roleId;


}
