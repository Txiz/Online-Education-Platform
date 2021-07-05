package com.xzx.extension.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzx.common.result.R;
import com.xzx.extension.entity.Banner;
import com.xzx.extension.mapper.BannerMapper;
import com.xzx.extension.service.BannerService;
import com.xzx.extension.vo.BannerVo;
import org.springframework.beans.BeanUtils;
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
 * @since 2021-03-29
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 删除所有的缓存
     */
    private void deleteRedisKey() {
        redisTemplate.delete("banner_web_list");
    }

    /**
     * 保存轮播图
     *
     * @param banner 轮播图数据
     * @return 统一结果封装
     */
    @Override
    public R saveBanner(Banner banner) {
        if (banner == null)
            return R.error().message("轮播图信息不能全部为空!");
        save(banner);
        deleteRedisKey();
        return R.ok().message("保存轮播图成功!");
    }

    /**
     * 分页查询轮播图
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 统一结果封装
     */
    @Override
    public R pageBanner(Integer current, Integer size) {
        Page<Banner> bannerPage = new Page<>(current, size);
        page(bannerPage);
        Map<String, Object> map = new HashMap<>();
        map.put("total", bannerPage.getTotal());
        map.put("bannerList", bannerPage.getRecords());
        return R.ok().data(map).message("分页查询轮播图成功!");
    }

    /**
     * 根据id删除轮播图
     *
     * @param bannerId 轮播图id
     * @return 统一结果封装
     */
    @Override
    public R deleteById(Integer bannerId) {
        deleteRedisKey();
        return removeById(bannerId) ? R.ok().message("删除轮播图成功!") : R.error().message("删除轮播图失败!");
    }

    /**
     * 前台方法——查询轮播图列表
     *
     * @return 统一结果封装
     */
    @Override
    public R listBanner() {
        List<BannerVo> bannerVos = (List<BannerVo>) redisTemplate.opsForValue().get("banner_web_list");
        if (ObjectUtils.isEmpty(bannerVos)) {
            List<Banner> banners = list();
            bannerVos = new ArrayList<>();
            for (Banner banner : banners) {
                BannerVo bannerVo = new BannerVo();
                BeanUtils.copyProperties(banner, bannerVo);
                bannerVos.add(bannerVo);
            }
            redisTemplate.opsForValue().set("banner_web_list", bannerVos);
        }
        return R.ok().data("bannerList", bannerVos).message("查询轮播图列表成功!");
    }
}
