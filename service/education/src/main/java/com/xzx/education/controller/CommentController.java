package com.xzx.education.controller;


import com.xzx.common.result.R;
import com.xzx.education.annotation.OperateLogger;
import com.xzx.education.entity.Comment;
import com.xzx.education.service.CommentService;
import com.xzx.education.vo.CommentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xzx
 * @since 2021-03-24
 */
@RestController
@RequestMapping("/education/comment")
@Api(tags = "评论控制器")
public class CommentController {

    @Resource
    private CommentService commentService;

    @Transactional
    @OperateLogger(description = "保存或更新评论")
    @ApiOperation(value = "保存或更新评论")
    @PostMapping("/saveOrUpdateComment")
    public R saveOrUpdateComment(@RequestBody CommentVo commentVo) {
        if (commentVo == null)
            return R.error().message("评论信息不能全部为空");
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentVo, comment);
        if (commentVo.getParentId() == null) {
            comment.setParentId(0);
        }
        commentService.saveOrUpdate(comment);
        return R.ok().message("保存或更新评论成功!");
    }

    @OperateLogger(description = "分页查询所有评论")
    @ApiOperation(value = "分页查询所有评论")
    @GetMapping("/pageComment/{current}/{size}")
    public R pageComment(@PathVariable Integer current, @PathVariable Integer size) {
        return R.ok().data(commentService.pageComment(current, size)).message("分页查询所有评论成功!");
    }

    @Transactional
    @OperateLogger(description = "根据评论id删除评论")
    @ApiOperation(value = "根据评论id删除评论")
    @DeleteMapping("/deleteCommentById/{commentId}")
    public R deleteCommentById(@PathVariable Integer commentId) {
        return commentService.deleteCommentById(commentId) ? R.ok().message("删除评论成功!") : R.error().message("删除评论失败!");
    }

    @OperateLogger(description = "根据课程id查询所有的评论")
    @ApiOperation(value = "根据课程id查询所有的评论")
    @GetMapping("/listAllCommentByCourseId/{courseId}")
    public R listAllCommentByCourseId(@PathVariable Integer courseId) {
        return R.ok().data("commentList", commentService.listAllCommentByCourseId(courseId)).message("根据课程id查询所有的评论!");
    }
}

