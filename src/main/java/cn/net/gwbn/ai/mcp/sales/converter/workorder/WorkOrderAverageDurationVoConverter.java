package cn.net.gwbn.ai.mcp.sales.converter.workorder;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderAverageDurationVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderStatisticVo;
import cn.net.gwbn.ai.mcp.sales.converter.IConverter;

/**
 * @author lixiaofeng
 * @date 9/14/26 PM4:58
 **/
public interface WorkOrderAverageDurationVoConverter  extends IConverter<QueryResult, WorkOrderAverageDurationVo> {

    /**
     * 转换工单平均完成时长统计结果.
     *
     * @param year        年
     * @param month       月
     * @param day         日
     * @param dayResult   当天查询结果
     * @param monthResult 当月查询结果
     * @return 平均完成时长统计结果
     */
    WorkOrderAverageDurationVo convert(int year, int month, int day, QueryResult dayResult, QueryResult monthResult);
}