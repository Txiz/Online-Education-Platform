package com.xzx.education.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 分类树视图
 * 作者: xzx
 * 创建时间: 2021-03-20-20-17
 **/
@Data
@ApiModel(value = "分类树视图", description = "")
public class CategoryTreeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程分类主键id")
    private Integer categoryId;
    @ApiModelProperty(value = "课程分类名称")
    private String categoryName;
    @ApiModelProperty(value = "父分类id")
    private Integer parentId;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "子分类")
    private List<CategoryTreeVo> children;
}
