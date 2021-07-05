package com.xzx.education.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xzx.common.result.R;
import com.xzx.education.annotation.OperateLogger;
import com.xzx.education.entity.Chapter;
import com.xzx.education.entity.Video;
import com.xzx.education.service.ChapterService;
import com.xzx.education.service.VideoService;
import com.xzx.education.vo.ChapterVo;
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
 * @since 2021-03-17
 */
@RestController
@RequestMapping("/education/chapter")
@Api(tags = "章节控制器")
public class ChapterController {

    @Resource
    private ChapterService chapterService;

    @Resource
    private VideoService videoService;

    @Transactional
    @OperateLogger(description = "保存或者更新章节")
    @ApiOperation(value = "保存或者更新章节")
    @PostMapping("/saveOrUpdateChapter")
    public R saveOrUpdateChapter(@RequestBody ChapterVo chapterVo) {
        if (chapterVo == null)
            return R.error().message("章节信息不能全部为空");
        Chapter chapter = new Chapter();
        BeanUtils.copyProperties(chapterVo, chapter);
        chapterService.saveOrUpdate(chapter);
        Video video = new Video();
        video.setChapterId(chapter.getChapterId());
        video.setVideoId(chapterVo.getVideoId());
        video.setVideoUrl(chapterVo.getVideoUrl());
        video.setVideoName(chapterVo.getVideoName());
        videoService.saveOrUpdate(video);
        return R.ok().message("保存或更新章节成功!");
    }

    @OperateLogger(description = "分页查询章节列表")
    @ApiOperation(value = "分页查询章节列表")
    @GetMapping("/pageChapter/{courseId}/{current}/{size}")
    public R pageChapter(@PathVariable Integer courseId, @PathVariable Integer current, @PathVariable Integer size) {
        return R.ok().data(chapterService.pageChapter(courseId, current, size)).message("分页查询章节列表成功!");
    }

    @Transactional
    @OperateLogger(description = "根据id删除章节")
    @ApiOperation(value = "根据id删除章节")
    @DeleteMapping("/deleteChapterById/{chapterId}")
    public R deleteChapterById(@PathVariable Integer chapterId) {
        Integer parentId = chapterService.getById(chapterId).getParentId();
        QueryWrapper<Chapter> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("parent_id", chapterId);
        if (parentId.equals(0) && chapterService.count(wrapper1) > 0) return R.error().message("该父章节下有子章节，无法删除!");
        return chapterService.deleteChapterById(chapterId) ? R.ok().message("章节删除成功!") : R.error().message("章节删除失败!");
    }

    @OperateLogger(description = "查询所有父章节")
    @ApiOperation(value = "查询所有父章节")
    @GetMapping("/listAllParentChapter/{courseId}")
    public R listAllParentChapter(@PathVariable Integer courseId) {
        return R.ok().data("parentChapter", chapterService.listAllParentChapter(courseId)).message("查询所有父章节成功!");
    }
}

