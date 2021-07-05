package com.xzx.extension.service.impl;

import com.alibaba.fastjson.JSON;
import com.xzx.common.result.R;
import com.xzx.extension.client.EducationClient;
import com.xzx.extension.service.ElasticSearchService;
import com.xzx.extension.vo.CourseCardVo;
import com.xzx.extension.vo.SearchParamVo;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: xzx
 * 创建时间: 2021-04-10-22-41
 **/
@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {

    @Resource
    private EducationClient educationClient;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void putCourseCardVoToElasticSearch() {
        // 服务调用，获取课程卡片视图列表，并上传至ES
        List<CourseCardVo> courseCardVos = educationClient.setAllCourseCardVo();
        for (CourseCardVo courseCardVo : courseCardVos) {
            IndexRequest indexRequest = new IndexRequest("course_card_vos");
            indexRequest.id(courseCardVo.getCourseId().toString());
            indexRequest.source(JSON.toJSONString(courseCardVo), XContentType.JSON);
            IndexResponse response = null;
            try {
                response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("response = " + response);
        }
    }

    @Override
    public R searchCourseCardVo(SearchParamVo searchParamVo) {
        // 1. 准备搜索请求参数
        SearchRequest searchRequest = buildSearchRequest(searchParamVo);
        // 2. 执行搜索请求
        SearchResponse searchResponse;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(searchResponse);
        } catch (IOException e) {
            e.printStackTrace();
            return R.error().message("搜索请求执行失败!详情请查看异常");
        }
        if (searchResponse == null) {
            return R.error().message("搜索请求执行失败!详情请查看异常");
        }
        // 3. 分析请求结果
        Map<String, Object> map = buildSearchResponse(searchResponse, searchParamVo.getCurrent());
        return R.ok().data(map).message("搜索成功!");
    }

    @Override
    public R getNewCourseCardVo() {
        SearchParamVo searchParamVo = new SearchParamVo();
        searchParamVo.setCurrent(1);
        searchParamVo.setPublish(true);
        searchParamVo.setCommentNum(false);
        searchParamVo.setStudentNum(false);
        return searchCourseCardVo(searchParamVo);
    }

    private SearchRequest buildSearchRequest(SearchParamVo searchParamVo) {
        String keyword = searchParamVo.getKeyword();
        Integer categoryId = searchParamVo.getCategoryId();
        Boolean publish = searchParamVo.getPublish();
        Boolean studentNum = searchParamVo.getStudentNum();
        Boolean commentNum = searchParamVo.getCommentNum();
        Integer current = searchParamVo.getCurrent();

        // 构建DSL语句
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // bool query
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // bool query——must
        if (!StringUtils.isEmpty(keyword))
            boolQueryBuilder.must(QueryBuilders.matchQuery("courseName", keyword));
        // bool query——filter
        if (categoryId != null)
            boolQueryBuilder.filter(QueryBuilders.termQuery("categoryId", categoryId));
        searchSourceBuilder.query(boolQueryBuilder);

        // 排序
        if (publish) searchSourceBuilder.sort("createTime", SortOrder.DESC);
        if (studentNum) searchSourceBuilder.sort("studentNum", SortOrder.DESC);
        if (commentNum) searchSourceBuilder.sort("commentNum", SortOrder.DESC);

        // 分页，每页8个
        searchSourceBuilder.from((current - 1) * SearchParamVo.size);
        searchSourceBuilder.size(SearchParamVo.size);

        // 高亮
        if (!StringUtils.isEmpty(keyword)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("courseName");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }
        return new SearchRequest(new String[]{"course_card_vos"}, searchSourceBuilder);
    }

    private Map<String, Object> buildSearchResponse(SearchResponse searchResponse, Integer current) {
        Map<String, Object> map = new HashMap<>();
        map.put("current", current);
        SearchHits hits = searchResponse.getHits();
        // 总记录数
        Long total = hits.getTotalHits().value;
        map.put("total", total);
        // 总页码
        Integer pages = (int) (total % SearchParamVo.size == 0 ? total / SearchParamVo.size : (total / SearchParamVo.size + 1));
        map.put("pages", pages);
        // 封装结果
        List<CourseCardVo> courseCardVos = new ArrayList<>();
        if (hits.getHits() != null && hits.getHits().length > 0) {
            for (SearchHit hit : hits.getHits()) {
                String string = hit.getSourceAsString();
                CourseCardVo courseCardVo = JSON.parseObject(string, CourseCardVo.class);
                courseCardVos.add(courseCardVo);
            }
        }
        map.put("courseCardVos", courseCardVos);
        // 是否有前一页，后一页
        boolean hasPrevious = true;
        boolean hasNext = true;
        if (current.equals(1)) hasPrevious = false;
        if (current.equals(pages)) hasNext = false;
        map.put("hasPrevious", hasPrevious);
        map.put("hasNext", hasNext);
        return map;
    }
}
