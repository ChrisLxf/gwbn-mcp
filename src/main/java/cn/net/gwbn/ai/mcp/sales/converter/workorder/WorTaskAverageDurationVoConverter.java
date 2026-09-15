package cn.net.gwbn.ai.mcp.sales.converter.workorder;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorTaskAverageDurationVo;
import cn.net.gwbn.ai.mcp.sales.converter.SummaryVoConverter;

/**
 * 工单任务平均完成时长转换器.
 *
 * @author lixiaofeng
 * @date 9/15/26
 */
public interface WorTaskAverageDurationVoConverter extends SummaryVoConverter<WorTaskAverageDurationVo> {

    WorTaskAverageDurationVo convert(int year, int month, int day, QueryResult dayResult, QueryResult monthResult);
}