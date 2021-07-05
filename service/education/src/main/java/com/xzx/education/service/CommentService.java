package com.xzx.education.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzx.education.entity.Comment;
import com.xzx.education.vo.CommentVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xzx
 * @since 2021-03-24
 */
public interface CommentService extends IService<Comment> {

    /**
     * 分页查询分类列表
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    Map<String, Object> pageComment(Integer current, Integer size);

    /**
     * 根据评论id删除评论
     *
     * @param commentId 分类id
     * @return 是否删除成功
     */
    boolean deleteCommentById(Integer commentId);

    /**
     * 根据课程id查询所有评论
     *
     * @param courseId 课程id
     * @return 视图列表
     */
    List<CommentVo> listAllCommentByCourseId(Integer courseId);

    /**
     * 根据课程id删除评论
     *
     * @param courseId 课程id
     */
    void deleteByCourseId(Integer courseId);
}
