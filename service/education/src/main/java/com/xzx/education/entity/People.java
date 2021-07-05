package com.xzx.education.entity;

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
@TableName("tb_people")
@ApiModel(value = "People对象", description = "")
public class People implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "教师或学生的主键id")
    @TableId(value = "people_id", type = IdType.AUTO)
    private Integer peopleId;

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
