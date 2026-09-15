package cn.net.gwbn.ai.mcp.sales.converter.workorder;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.MassOrderCompletionRateVo;
import cn.net.gwbn.ai.mcp.sales.converter.SummaryVoConverter;

/**
 * 批量工单完成率转换器.
 *
 * @author lixiaofeng
 * @date 9/15/26
 **/
public interface MassOrderCompletionRateVoConverter
        extends SummaryVoConverter<MassOrderCompletionRateVo> {

    /**
     * 转换完成率统计结果.
     *
     * @param year                 年
     * @param month                月
     * @param day                  日
     * @param dayCompletedResult   当日已完成
     * @param dayTotalResult       当日全部
     * @param monthCompletedResult 当月已完成
     * @param monthTotalResult     当月全部
     * @return 完成率统计结果
     */
    MassOrderCompletionRateVo convert(int year, int month, int day, QueryResult dayCompletedResult, QueryResult dayTotalResult, QueryResult monthCompletedResult, QueryResult monthTotalResult);
}