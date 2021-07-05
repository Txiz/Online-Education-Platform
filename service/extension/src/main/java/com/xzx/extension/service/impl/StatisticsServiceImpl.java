package com.xzx.extension.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzx.common.result.R;
import com.xzx.extension.entity.Statistics;
import com.xzx.extension.mapper.StatisticsMapper;
import com.xzx.extension.service.StatisticsService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
 * @since 2021-04-12
 */
@Service
public class StatisticsServiceImpl extends ServiceImpl<StatisticsMapper, Statistics> implements StatisticsService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 删除redis缓存
     */
    private void deleteRedisKey() {
        redisTemplate.delete("statistics_list");
        redisTemplate.delete("statistics_total");
        redisTemplate.delete("statistics_current");
        redisTemplate.delete("statistics_size");
    }


    /**
     * 检查redis中key是否为空
     *
     * @param statisticsList    统计列表
     * @param statisticsTotal   统计总数
     * @param statisticsCurrent 当前页
     * @param statisticsSize    每页大小
     * @return 是否存在有key不存在
     */
    private boolean checkRedisKey(List<Statistics> statisticsList, Integer statisticsTotal, Integer statisticsCurrent, Integer statisticsSize) {
        return ObjectUtils.isEmpty(statisticsList) ||
                ObjectUtils.isEmpty(statisticsTotal) ||
                ObjectUtils.isEmpty(statisticsCurrent) ||
                ObjectUtils.isEmpty(statisticsSize);
    }


    /**
     * 保存统计数据进入数据库
     *
     * @param statistics 统计数据
     */
    @Override
    public void saveStatistics(Statistics statistics) {
        if (statistics == null) {
            R.error().message("统计信息不能全部为空!");
            return;
        }
        save(statistics);
        deleteRedisKey();
        R.ok().message("保存统计数据成功!");
    }

    /**
     * 分页查询统计数据
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 统一结果封装
     */
    @Override
    public R pageStatistics(Integer current, Integer size) {
        List<Statistics> statisticsList = (List<Statistics>) redisTemplate.opsForValue().get("statistics_list");
        Integer statisticsTotal = (Integer) redisTemplate.opsForValue().get("statistics_total");
        Integer statisticsCurrent = (Integer) redisTemplate.opsForValue().get("statistics_current");
        Integer statisticsSize = (Integer) redisTemplate.opsForValue().get("statistics_size");
        if (checkRedisKey(statisticsList, statisticsTotal, statisticsCurrent, statisticsSize) || !current.equals(statisticsCurrent) || !size.equals(statisticsSize)) {
            Page<Statistics> statisticsPage = new Page<>(current, size);
            page(statisticsPage, null);
            statisticsList = statisticsPage.getRecords();
            statisticsTotal = Math.toIntExact(statisticsPage.getTotal());
            redisTemplate.opsForValue().set("statistics_list", statisticsList);
            redisTemplate.opsForValue().set("statistics_total", statisticsTotal);
            redisTemplate.opsForValue().set("statistics_current", statisticsPage.getCurrent());
            redisTemplate.opsForValue().set("statistics_size", statisticsPage.getSize());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", statisticsTotal);
        map.put("statisticsList", statisticsList);
        return R.ok().data(map).message("分页查询统计数据成功!");
    }

    /**
     * 根据id删除统计数据
     *
     * @param statisticsId 统计数据
     * @return 统一结果封装
     */
    @Override
    public R deleteById(Integer statisticsId) {
        deleteRedisKey();
        return removeById(statisticsId) ? R.ok().message("删除统计数据成功!") : R.error().message("删除统计数据失败!");
    }

    /**
     * 从redis中获取当日临时统计数据
     *
     * @return 统一结果封装
     */
    @Override
    public R getCurrentStatisticsNum() {
        Integer loginNum = (Integer) redisTemplate.opsForValue().get("login_num");
        Integer exceptionNum = (Integer) redisTemplate.opsForValue().get("exception_num");
        Integer registerNum = (Integer) redisTemplate.opsForValue().get("register_num");
        // 判断是否是null
        if (loginNum == null) loginNum = 0;
        if (exceptionNum == null) exceptionNum = 0;
        if (registerNum == null) registerNum = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("loginNum", loginNum);
        map.put("registerNum", registerNum);
        map.put("exceptionNum", exceptionNum);
        return R.ok().data(map).message("从redis中获取当日临时统计数据成功!");
    }

    /**
     * 获取统计数据用于制作地图
     *
     * @return 统一结果封装
     */
    @Override
    public R getStatisticsDataForMap() {
        QueryWrapper<Statistics> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("statistics_date");
        wrapper.last("limit 14");
        List<Statistics> list = list(wrapper);
        List<String> date = new ArrayList<>();
        List<Integer> loginNums = new ArrayList<>();
        List<Integer> registerNums = new ArrayList<>();
        List<Integer> exceptionNums = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            date.add(list.get(i).getStatisticsDate());
            loginNums.add(list.get(i).getLoginNum());
            registerNums.add(list.get(i).getRegisterNum());
            exceptionNums.add(list.get(i).getExceptionNum());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("date", date);
        map.put("loginNums", loginNums);
        map.put("registerNums", registerNums);
        map.put("exceptionNums", exceptionNums);
        return R.ok().data(map).message("获取地图数据成功");
    }
}
