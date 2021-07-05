package com.xzx.extension.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 搜索参数
 * 作者: xzx
 * 创建时间: 2021-04-11-10-56
 **/
@Data
@ApiModel(value = "搜索参数", description = "")
public class SearchParamVo implements Serializable {

    public static final Integer size = 8;
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "搜索关键字")
    private String keyword;
    @ApiModelProperty(value = "课程分类id")
    private Integer categoryId;
    @ApiModelProperty(value = "最新发布")
    private Boolean publish;
    @ApiModelProperty(value = "选课人数")
    private Boolean studentNum;
    @ApiModelProperty(value = "评论人数")
    private Boolean commentNum;
    @ApiModelProperty(value = "当前页")
    private Integer current = 1;
}
