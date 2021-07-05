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
 * @since 2021-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_exception_log")
@ApiModel(value = "ExceptionLog对象", description = "")
public class ExceptionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "日志主键id")
    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;

    @ApiModelProperty(value = "用户名称")
    private String username;

    @ApiModelProperty(value = "请求接口")
    private String uri;

    @ApiModelProperty(value = "请求方法")
    private String method;

    @ApiModelProperty(value = "方法描述")
    private String description;

    @ApiModelProperty(value = "异常信息")
    private String error;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "IP来源")
    private String ipSource;

    @ApiModelProperty(value = "操作系统")
    private String os;

    @ApiModelProperty(value = "浏览器")
    private String browser;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
