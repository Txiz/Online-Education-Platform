package com.xzx.education.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
@TableName("tb_people_course")
@ApiModel(value = "PeopleCourse对象", description = "")
public class PeopleCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "教师或学生id")
    private Integer peopleId;

    @ApiModelProperty(value = "课程id")
    private Integer courseId;


}
