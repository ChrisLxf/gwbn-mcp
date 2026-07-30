package cn.net.gwbn.ai.mcp.sales.converter;

import cn.net.gwbn.ai.mcp.sales.api.SummaryVieObject;
import cn.net.gwbn.ai.mcp.engine.result.QueryResult;

/**
 * @author lixiaofeng
 * @date 6/28/26 AM10:48
 **/
public interface SummaryVoConverter<T extends SummaryVieObject> extends IConverter<QueryResult, T> {
}

