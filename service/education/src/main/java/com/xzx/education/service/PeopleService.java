package com.xzx.education.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzx.education.entity.People;
import com.xzx.education.vo.PeopleExcelVo;
import com.xzx.education.vo.PeopleSearchVo;
import org.springframework.web.multipart.MultipartFile;

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
public interface PeopleService extends IService<People> {

    /**
     * 使用Excel批量导入账号
     *
     * @param file          Excel文件
     * @param peopleService 账号服务
     */
    void savePeopleByUseExcel(MultipartFile file, PeopleService peopleService);

    /**
     * Excel中的单行数据视图
     *
     * @param peopleExcelVo 账号Excel表视图
     */
    void savePeopleByUseExcel(PeopleExcelVo peopleExcelVo);

    /**
     * 分页查询账号列表
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    Map<String, Object> pagePeople(Integer current, Integer size);

    /**
     * 模糊查询账号
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 结果集
     */
    Map<String, Object> searchPeople(Integer current, Integer size, PeopleSearchVo peopleSearchVo);

    /**
     * 根据账号id删除账号
     *
     * @param peopleId 账号id
     * @return 是否删除成功!
     */
    boolean deletePeopleById(Integer peopleId);

    /**
     * 查询所有的教师账号
     *
     * @param current 当前页
     * @return 查询所有的教师账号
     */
    Map<String, Object> listAllTeacher(Integer current);

    /**
     * 前台方法——查询最热门教师
     *
     * @return 查询所有的教师账号
     */
    List<People> listHotTeacher();

    /**
     * 前台方法——查询某个人
     *
     * @param peopleId 人id
     * @return 结果集
     */
    Map<String, Object> getByPeopleId(Integer peopleId);

    Map<String, Object> getByPeopleName(String peopleName);
}
