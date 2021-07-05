package com.xzx.education.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.xzx.education.service.PeopleService;
import com.xzx.education.vo.PeopleExcelVo;

/**
 * 从Excel中读取并且批量插入账号
 * 作者: xzx
 * 创建时间: 2021-03-30-22-38
 **/
public class PeopleListener extends AnalysisEventListener<PeopleExcelVo> {

    public PeopleService peopleService;

    public PeopleListener() {
    }

    public PeopleListener(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public void invoke(PeopleExcelVo peopleExcelVo, AnalysisContext analysisContext) {
        if (peopleExcelVo == null) throw new RuntimeException("Excel表中无数据!");
        try {
            peopleService.savePeopleByUseExcel(peopleExcelVo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
