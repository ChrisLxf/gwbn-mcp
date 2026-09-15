package cn.net.gwbn.ai.mcp.sales.converter.workorder;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.MassOrderStatisticVo;
import cn.net.gwbn.ai.mcp.sales.converter.IConverter;

/**
 * 批量工单统计结果转换器
 *
 * @author lixiaofeng
 * @date 9/14/26 PM4:20
 **/
public interface MassOrderStatisticVoConverter extends IConverter<QueryResult, MassOrderStatisticVo> {

    /**
     * 转换统计结果.
     *
     * @param year        年
     * @param month       月
     * @param day         日
     * @param dayResult   当天查询结果
     * @param monthResult 当月查询结果
     * @return 批量工单统计结果
     */
    MassOrderStatisticVo convert(int year, int month, int day, QueryResult dayResult, QueryResult monthResult);
}
