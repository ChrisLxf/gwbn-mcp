package cn.net.gwbn.ai.mcp.sales.converter.workorder;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorTaskAverageDurationVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkTaskCountStatisticVo;
import cn.net.gwbn.ai.mcp.sales.converter.SummaryVoConverter;

/**
 * 工单任务平均完成时长转换器.
 *
 * @author lixiaofeng
 * @date 9/15/26
 */
public interface WorTaskAverageDurationVoConverter extends SummaryVoConverter<WorTaskAverageDurationVo> {

    WorTaskAverageDurationVo convert(int year, int month, int day, QueryResult dayResult, QueryResult monthResult);


    /**
     * 转换当日和当月统计结果.
     *
     * @param year        年
     * @param month       月
     * @param day         日
     * @param dimension   统计维度
     * @param dayResult   当日查询结果
     * @param monthResult 当月查询结果
     * @return 工单任务数量统计结果
     */
    WorkTaskCountStatisticVo convert(int year, int month, int day, String dimension, QueryResult dayResult, QueryResult monthResult);
}