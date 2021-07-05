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
@TableName("tb_chapter")
@ApiModel(value = "Chapter对象", description = "")
public class Chapter implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "章节主键id")
    @TableId(value = "chapter_id", type = IdType.AUTO)
    private Integer chapterId;

    @ApiModelProperty(value = "章节名称")
    private String chapterName;

    @ApiModelProperty(value = "课程id")
    private Integer courseId;

    @ApiModelProperty(value = "父id")
    private Integer parentId;

    @ApiModelProperty(value = "章节排序")
    private Integer sort;

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
