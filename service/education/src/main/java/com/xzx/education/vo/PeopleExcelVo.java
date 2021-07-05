package com.xzx.education.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 账号Excel表视图
 * 作者: xzx
 * 创建时间: 2021-03-30-22-39
 **/
@Data
@ApiModel(value = "账号Excel表视图", description = "")
public class PeopleExcelVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(index = 0)
    @ApiModelProperty(value = "教师或学生的用户名	")
    private String peopleName;

    @ExcelProperty(index = 1)
    @ApiModelProperty(value = "教师或学生的姓名")
    private String nickName;

    @ExcelProperty(index = 2)
    @ApiModelProperty(value = "教师还是学生")
    private String isTeacherOrStudent;
}
