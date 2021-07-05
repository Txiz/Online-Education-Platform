package com.xzx.education.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzx.education.entity.Comment;
import com.xzx.education.mapper.CommentMapper;
import com.xzx.education.mapper.CourseMapper;
import com.xzx.education.mapper.PeopleMapper;
import com.xzx.education.service.CommentService;
import com.xzx.education.vo.CommentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xzx
 * @since 2021-03-24
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private PeopleMapper peopleMapper;

    @Resource
    private CourseMapper courseMapper;

    /**
     * 分页查询分类列表
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    @Override
    public Map<String, Object> pageComment(Integer current, Integer size) {
        Page<Comment> commentPage = new Page<>(current, size);
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0);
        commentMapper.selectPage(commentPage, wrapper);
        List<Comment> comments = commentMapper.selectList(null);
        List<CommentVo> commentVos = new ArrayList<>();
        for (Comment comment : commentPage.getRecords()) {
            CommentVo commentVo = getCommentVoById(comment);
            List<CommentVo> children = setChildren(comments, comment.getCommentId());
            commentVo.setChildren(children);
            commentVos.add(commentVo);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", commentPage.getTotal());
        map.put("commentList", commentVos);
        return map;
    }

    /**
     * 根据评论id删除评论
     *
     * @param commentId 分类id
     * @return 是否删除成功
     */
    @Override
    public boolean deleteCommentById(Integer commentId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", commentId);
        List<Comment> comments = commentMapper.selectList(wrapper);
        if (comments.size() <= 0) {
            QueryWrapper<Comment> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("comment_id", commentId);
            commentMapper.deleteById(commentId);
            return true;
        }
        for (Comment comment : comments) {
            deleteCommentById(comment.getCommentId());
        }
        return commentMapper.deleteById(commentId) > 0;
    }

    /**
     * 根据课程id查询所有评论
     *
     * @param courseId 课程id
     * @return 视图列表
     */
    @Override
    public List<CommentVo> listAllCommentByCourseId(Integer courseId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.orderByDesc("create_time");
        List<Comment> comments = commentMapper.selectList(wrapper);
        List<CommentVo> res = new ArrayList<>();
        for (Comment comment : comments) {
            CommentVo commentVo = getCommentVoById(comment);
            res.add(commentVo);
        }
        return res;
    }

    /**
     * 根据课程id删除评论
     *
     * @param courseId 课程id
     */
    @Override
    public void deleteByCourseId(Integer courseId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0);
        wrapper.eq("course_id", courseId);
        List<Comment> comments = commentMapper.selectList(wrapper);
        for (Comment comment : comments) {
            deleteCommentById(comment.getCommentId());
        }
    }

    /**
     * 递归方法，构造递归子评论
     *
     * @param comments 评论列表
     * @param parentId 父菜单id
     * @return 评论视图列表
     */
    private List<CommentVo> setChildren(List<Comment> comments, Integer parentId) {
        List<CommentVo> res = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getParentId().equals(parentId)) {
                CommentVo commentVo = getCommentVoById(comment);
                List<CommentVo> children = setChildren(comments, comment.getCommentId());
                commentVo.setChildren(children);
                res.add(commentVo);
            }
        }
        return res;
    }

    /**
     * 根据评论对象封装评论视图
     *
     * @param comment 评论
     * @return 评论视图
     */
    private CommentVo getCommentVoById(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        String peopleNickName = peopleMapper.selectById(comment.getPeopleId()).getNickName();
        String courseName = courseMapper.selectById(comment.getCourseId()).getCourseName();
        String parentNickName = "";
        if (!comment.getParentId().equals(0)) {
            Integer parentId = comment.getParentId();
            Comment parentComment = commentMapper.selectById(parentId);
            parentNickName = peopleMapper.selectById(parentComment.getPeopleId()).getNickName();
        }
        commentVo.setPeopleNickName(peopleNickName);
        commentVo.setCourseName(courseName);
        commentVo.setParentNickName(parentNickName);
        return commentVo;
    }
}
