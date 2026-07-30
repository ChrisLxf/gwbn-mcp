package cn.net.gwbn.ai.mcp.sales.converter.business;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.business.BusinessSummaryVo;
import cn.net.gwbn.ai.mcp.sales.converter.IConverter;

/**
 * @author lixiaofeng
 * @date 6/28/26 PM5:02
 **/
public interface BusinessIncomeSummaryVoConverter extends IConverter<QueryResult, BusinessSummaryVo> {


    BusinessSummaryVo convert(int year, int month, int day, String type, QueryResult cityDayIncomeSummary, QueryResult cityMonthIncomeSummary);
}
