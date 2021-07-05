package com.xzx.education.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzx.education.client.VodClient;
import com.xzx.education.entity.Chapter;
import com.xzx.education.entity.Video;
import com.xzx.education.mapper.ChapterMapper;
import com.xzx.education.mapper.VideoMapper;
import com.xzx.education.service.ChapterService;
import com.xzx.education.vo.ChapterVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
 * @since 2021-03-17
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Resource
    private ChapterMapper chapterMapper;

    @Resource
    private VideoMapper videoMapper;

    @Resource
    private VodClient vodClient;

    /**
     * 分页查询当前课程下的章节列表
     *
     * @param courseId 课程id
     * @param current  当前页
     * @param size     每页大小
     * @return 结果集
     */
    @Override
    public Map<String, Object> pageChapter(Integer courseId, Integer current, Integer size) {
        QueryWrapper<Chapter> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("parent_id", 0);
        wrapper1.eq("course_id", courseId);
        Page<Chapter> chapterPage = new Page<>(current, size);
        chapterMapper.selectPage(chapterPage, wrapper1);
        QueryWrapper<Chapter> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("course_id", courseId);
        List<Chapter> chapters = chapterMapper.selectList(wrapper2);
        List<ChapterVo> chapterVos = new ArrayList<>();
        for (Chapter chapter : chapterPage.getRecords()) {
            ChapterVo chapterVo = getChapterVoById(chapter);
            List<ChapterVo> children = setChildren(chapters, chapter.getChapterId());
            chapterVo.setChildren(children);
            chapterVos.add(chapterVo);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", chapterPage.getTotal());
        map.put("chapterList", chapterVos);
        return map;
    }

    /**
     * 根据章节id删除章节
     *
     * @param chapterId 章节id
     * @return 是否删除成功
     */
    @Override
    public boolean deleteChapterById(Integer chapterId) {
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", chapterId);
        for (Video video : videoMapper.selectList(wrapper)) {
            videoMapper.deleteById(video.getVideoId());
            if (!StringUtils.isEmpty(video.getVideoUrl()))
                vodClient.remove(video.getVideoUrl());
        }
        return chapterMapper.deleteById(chapterId) > 0;
    }

    /**
     * 查询当前课程下的所有一级章节
     *
     * @return 章节列表
     */
    @Override
    public List<Chapter> listAllParentChapter(Integer courseId) {
        QueryWrapper<Chapter> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0);
        wrapper.eq("course_id", courseId);
        return chapterMapper.selectList(wrapper);
    }

    /**
     * 根据课程id删除章节
     *
     * @param courseId 课程id
     */
    @Override
    public void deleteByCourseId(Integer courseId) {
        QueryWrapper<Chapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        List<Chapter> chapters = chapterMapper.selectList(wrapper);
        for (Chapter chapter : chapters) {
            deleteChapterById(chapter.getChapterId());
        }
    }

    @Override
    public List<ChapterVo> listAllChapter(Integer courseId) {
        QueryWrapper<Chapter> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("parent_id", 0);
        wrapper1.eq("course_id", courseId);
        List<Chapter> chapterList = list(wrapper1);
        QueryWrapper<Chapter> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("course_id", courseId);
        List<Chapter> chapters = chapterMapper.selectList(wrapper2);
        List<ChapterVo> chapterVos = new ArrayList<>();
        for (Chapter chapter : chapterList) {
            ChapterVo chapterVo = getChapterVoById(chapter);
            List<ChapterVo> children = setChildren(chapters, chapter.getChapterId());
            chapterVo.setChildren(children);
            chapterVos.add(chapterVo);
        }
        return chapterVos;
    }

    /**
     * 递归方法，构造递归子章节
     *
     * @param chapters 章节列表
     * @param parentId 父章节id
     * @return 章节视图列表
     */
    private List<ChapterVo> setChildren(List<Chapter> chapters, Integer parentId) {
        List<ChapterVo> res = new ArrayList<>();
        for (Chapter chapter : chapters) {
            if (chapter.getParentId().equals(parentId)) {
                ChapterVo chapterVo = getChapterVoById(chapter);
                List<ChapterVo> children = setChildren(chapters, chapter.getChapterId());
                chapterVo.setChildren(children);
                res.add(chapterVo);
            }
        }
        return res;
    }

    /**
     * 根据章节对象封装章节视图
     *
     * @param chapter 章节
     * @return 章节视图
     */
    private ChapterVo getChapterVoById(Chapter chapter) {
        ChapterVo chapterVo = new ChapterVo();
        BeanUtils.copyProperties(chapter, chapterVo);
        if (chapter.getParentId().equals(0)) {
            chapterVo.setVideoId(null);
            chapterVo.setVideoUrl(null);
            chapterVo.setVideoName(null);
        } else {
            QueryWrapper<Video> wrapper = new QueryWrapper<>();
            wrapper.eq("chapter_id", chapter.getChapterId());
            Video video = videoMapper.selectOne(wrapper);
            chapterVo.setVideoId(video.getVideoId());
            chapterVo.setVideoUrl(video.getVideoUrl());
            chapterVo.setVideoName(video.getVideoName());
            if (chapterVo.getVideoUrl() != null)
                chapterVo.setVideoPlayAuth((String) vodClient.getPlayAuth(chapterVo.getVideoUrl()).getData().get("playAuth"));
        }
        return chapterVo;
    }
}
