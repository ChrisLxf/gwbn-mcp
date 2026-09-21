package cn.net.gwbn.ai.mcp.sales.converter.workorder;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderStatisticVo;
import cn.net.gwbn.ai.mcp.sales.converter.IConverter;

/**
 * 工单统计结果转换器
 *
 * @author lixiaofeng
 * @date 9/14/26 PM1:53
 **/
public interface WorkOrderStatisticVoConverter extends IConverter<QueryResult, WorkOrderStatisticVo> {

    /**
     * 转换工单统计结果.
     *
     * @param year        年
     * @param month       月
     * @param day         日
     * @param dimension   维度
     * @param dayResult   当天查询结果
     * @param monthResult 当月查询结果
     * @return 工单统计结果
     */
    WorkOrderStatisticVo convert(int year, int month, int day, String dimension, QueryResult dayResult, QueryResult monthResult);

    /**
     * 工单重复统计结果
     *
     * @param year
     * @param month
     * @param day
     * @param dimension
     * @param dayResult
     * @param monthResult
     * @return
     */
    WorkOrderStatisticVo convertDuplicate(int year, int month, int day, String dimension, QueryResult dayResult, QueryResult monthResult);
}