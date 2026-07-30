package cn.net.gwbn.ai.mcp.sales.converter.income;

import cn.net.gwbn.ai.mcp.sales.api.income.CityIncomeSummaryVo;
import cn.net.gwbn.ai.mcp.sales.converter.IConverter;
import cn.net.gwbn.ai.mcp.engine.result.QueryResult;

/**
 * @author lixiaofeng
 * @date 6/28/26 AM10:48
 **/
public interface CityIncomeSummaryVoConverter extends IConverter<QueryResult, CityIncomeSummaryVo> {


    /**
     * 转换为城市收入展示模型
     *
     * @param cityDayIncomeSummary   当日收入报表结果
     * @param cityMonthIncomeSummary 当月收入报表结果
     * @return 城市收入展示模型
     */
    CityIncomeSummaryVo convert(int year,int month,int day,QueryResult cityDayIncomeSummary, QueryResult cityMonthIncomeSummary);
}
