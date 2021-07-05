package com.xzx.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

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
@TableName("tb_api")
@ApiModel(value = "Api对象", description = "")
public class Api implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "接口主键id")
    @TableId(value = "api_id", type = IdType.AUTO)
    private Integer apiId;

    @ApiModelProperty(value = "接口名称")
    private String apiName;

    @ApiModelProperty(value = "接口路径")
    private String apiUrl;

    @ApiModelProperty(value = "需要授权 0：不需要  1：需要")
    private Boolean isAuth;

    @ApiModelProperty(value = "逻辑删除 0：未删除  1：已删除")
    @TableLogic
    private Boolean isDelete;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
