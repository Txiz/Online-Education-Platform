package com.xzx.extension.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzx.common.result.R;
import com.xzx.extension.entity.Banner;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xzx
 * @since 2021-03-29
 */
public interface BannerService extends IService<Banner> {

    /**
     * 保存轮播图
     *
     * @param banner 轮播图数据
     * @return 统一结果封装
     */
    R saveBanner(Banner banner);

    /**
     * 分页查询轮播图
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 统一结果封装
     */
    R pageBanner(Integer current, Integer size);

    /**
     * 根据id删除轮播图
     *
     * @param bannerId 轮播图id
     * @return 统一结果封装
     */
    R deleteById(Integer bannerId);

    /**
     * 前台方法——查询轮播图列表
     *
     * @return 统一结果封装
     */
    R listBanner();
}
