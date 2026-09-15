package cn.net.gwbn.ai.mcp.sales.converter.workorder;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.MassOrderAverageDurationVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.MassOrderStatisticVo;
import cn.net.gwbn.ai.mcp.sales.converter.IConverter;

/**
 * 非单户故障平均完成时长转换器.
 *
 * @author lixiaofeng
 * @date 9/14/26
 **/
public interface MassOrderAverageDurationVoConverter extends IConverter<QueryResult, MassOrderAverageDurationVo> {

    MassOrderAverageDurationVo convert(int year, int month, int day, QueryResult dayResult, QueryResult monthResult);
}