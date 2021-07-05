package com.xzx.education.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评论视图
 * 作者: xzx
 * 创建时间: 2021-03-26-13-07
 **/
@Data
@ApiModel(value = "评论视图", description = "")
public class CommentVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论主键id")
    private Integer commentId;
    @ApiModelProperty(value = "评论内容")
    private String commentContent;
    @ApiModelProperty(value = "用户id")
    private Integer peopleId;
    @ApiModelProperty(value = "用户昵称")
    private String peopleNickName;
    @ApiModelProperty(value = "课程id")
    private Integer courseId;
    @ApiModelProperty(value = "课程名称")
    private String courseName;
    @ApiModelProperty(value = "父评论id")
    private Integer parentId;
    @ApiModelProperty(value = "父评论昵称")
    private String parentNickName;
    @ApiModelProperty(value = "是否启用 0：未启用    1:已启用")
    private Boolean isEnable;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "子评论")
    private List<CommentVo> children;
}
