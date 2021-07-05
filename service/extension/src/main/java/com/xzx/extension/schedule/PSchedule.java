package com.xzx.extension.schedule;

import com.xzx.common.util.DateUtil;
import com.xzx.extension.entity.Statistics;
import com.xzx.extension.service.ElasticSearchService;
import com.xzx.extension.service.StatisticsService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 作者: xzx
 * 创建时间: 2021-04-09-19-24
 **/
@Component
public class PSchedule {

    @Resource
    private ElasticSearchService elasticSearchService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private StatisticsService statisticsService;

    /**
     * 将课程卡片视图信息存进elasticsearch中
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void task() {
        elasticSearchService.putCourseCardVoToElasticSearch();
    }

    /**
     * 获取前一天的统计数据并保存进数据库中
     */
//    @Scheduled(cron = "0/10 * * * * ?")
    public void task2() {
        // 从redis中获取值
        Integer loginNum = (Integer) redisTemplate.opsForValue().get("login_num");
        Integer exceptionNum = (Integer) redisTemplate.opsForValue().get("exception_num");
        Integer registerNum = (Integer) redisTemplate.opsForValue().get("register_num");
        // 判断是否是null
        if (loginNum == null) loginNum = 0;
        if (exceptionNum == null) exceptionNum = 0;
        if (registerNum == null) registerNum = 0;
        // 保存进数据库中
        // 获取前一天的日期
        String date = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        Statistics statistics = new Statistics();
        statistics.setStatisticsDate(date);
        statistics.setLoginNum(loginNum);
        statistics.setExceptionNum(exceptionNum);
        statistics.setRegisterNum(registerNum);
        statisticsService.saveStatistics(statistics);
        // 将redis中key的值重置为0
        redisTemplate.opsForValue().set("login_num", 0);
        redisTemplate.opsForValue().set("exception_num", 0);
        redisTemplate.opsForValue().set("register_num", 0);
    }
}
