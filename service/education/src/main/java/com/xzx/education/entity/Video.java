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
@TableName("tb_video")
@ApiModel(value = "Video对象", description = "")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "视频主键id")
    @TableId(value = "video_id", type = IdType.AUTO)
    private Integer videoId;

    @ApiModelProperty(value = "视频凭证（阿里云返回的视频id）")
    private String videoUrl;

    @ApiModelProperty(value = "视频名称")
    private String videoName;

    @ApiModelProperty(value = "章节id")
    private Integer chapterId;

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
