package com.xzx.extension.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 轮播图视图
 * 作者: xzx
 * 创建时间: 2021-04-08-11-48
 **/
@Data
@ApiModel(value = "轮播图视图", description = "")
public class BannerVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "轮播图id")
    private Integer bannerId;
    @ApiModelProperty(value = "轮播图路径")
    private String bannerUrl;
}
