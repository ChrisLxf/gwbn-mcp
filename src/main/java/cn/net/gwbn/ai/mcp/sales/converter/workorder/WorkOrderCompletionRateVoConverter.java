package cn.net.gwbn.ai.mcp.sales.converter.workorder;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderCompletionRateVo;
import cn.net.gwbn.ai.mcp.sales.converter.SummaryVoConverter;

/**
 * 工单完成率转换器.
 *
 * @author lixiaofeng
 * @date 9/15/26
 */
public interface WorkOrderCompletionRateVoConverter extends SummaryVoConverter<WorkOrderCompletionRateVo> {

    WorkOrderCompletionRateVo convert(int year, int month, int day, QueryResult dayCompletedResult, QueryResult dayTotalResult, QueryResult monthCompletedResult, QueryResult monthTotalResult);
}