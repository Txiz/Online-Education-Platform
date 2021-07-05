package com.xzx.extension.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzx.common.result.R;
import com.xzx.extension.entity.Statistics;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xzx
 * @since 2021-04-12
 */
public interface StatisticsService extends IService<Statistics> {

    /**
     * 保存统计数据进入数据库
     *
     * @param statistics 统计数据
     */
    void saveStatistics(Statistics statistics);

    /**
     * 分页查询统计数据
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 统一结果封装
     */
    R pageStatistics(Integer current, Integer size);

    /**
     * 根据id删除统计数据
     *
     * @param statisticsId 统计数据
     * @return 统一结果封装
     */
    R deleteById(Integer statisticsId);

    /**
     * 从redis中获取当日临时统计数据
     *
     * @return 统一结果封装
     */
    R getCurrentStatisticsNum();

    /**
     * 获取统计数据用于制作地图
     *
     * @return 统一结果封装
     */
    R getStatisticsDataForMap();
}
