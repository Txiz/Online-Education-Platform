package com.xzx.education.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzx.education.entity.Chapter;
import com.xzx.education.vo.ChapterVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xzx
 * @since 2021-03-17
 */
public interface ChapterService extends IService<Chapter> {

    /**
     * 分页查询当前课程下的章节列表
     *
     * @param courseId 课程id
     * @param current  当前页
     * @param size     每页大小
     * @return 结果集
     */
    Map<String, Object> pageChapter(Integer courseId, Integer current, Integer size);

    /**
     * 根据章节id删除章节
     *
     * @param chapterId 章节id
     * @return 是否删除成功
     */
    boolean deleteChapterById(Integer chapterId);

    /**
     * 查询当前课程下的所有一级章节
     *
     * @return 章节列表
     */
    List<Chapter> listAllParentChapter(Integer courseId);

    /**
     * 根据课程id删除章节
     *
     * @param courseId 课程id
     */
    void deleteByCourseId(Integer courseId);

    List<ChapterVo> listAllChapter(Integer courseId);
}
