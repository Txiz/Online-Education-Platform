package com.xzx.extension.service;

import com.xzx.common.result.R;
import com.xzx.extension.vo.SearchParamVo;

/**
 * 搜索服务
 * 作者: xzx
 * 创建时间: 2021-04-10-18-19
 **/
public interface ElasticSearchService {

    void putCourseCardVoToElasticSearch();

    R searchCourseCardVo(SearchParamVo searchParamVo);

    R getNewCourseCardVo();
}
