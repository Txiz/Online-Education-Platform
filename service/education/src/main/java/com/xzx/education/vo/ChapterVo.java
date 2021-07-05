package com.xzx.education.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 章节视图
 * 作者: xzx
 * 创建时间: 2021-03-20-17-09
 **/
@Data
@ApiModel(value = "章节视图", description = "")
public class ChapterVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "章节主键id")
    private Integer chapterId;
    @ApiModelProperty(value = "章节名称")
    private String chapterName;
    @ApiModelProperty(value = "课程id")
    private Integer courseId;
    @ApiModelProperty(value = "章节孩子")
    List<ChapterVo> children;
    @ApiModelProperty(value = "视频主键id")
    private Integer videoId;
    @ApiModelProperty(value = "视频凭证（阿里云返回的视频id）")
    private String videoUrl;
    @ApiModelProperty(value = "视频名称")
    private String videoName;
    @ApiModelProperty(value = "视频播放凭证")
    private String videoPlayAuth;
    @ApiModelProperty(value = "章节创建时间")
    private Date createTime;
    @ApiModelProperty(value = "章节更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "父id")
    private Integer parentId;
    @ApiModelProperty(value = "章节排序")
    private Integer sort;
}
