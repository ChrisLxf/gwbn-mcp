package cn.net.gwbn.ai.mcp.sales.converter.user;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.user.ExistUserSummaryVo;
import cn.net.gwbn.ai.mcp.sales.converter.IConverter;

/**
 * 在网用户汇总转换器
 *
 * @author lixiaofeng
 * @date 7/28/26 PM1:47
 **/
public interface ExistUserSummaryVoConverter extends IConverter<QueryResult, ExistUserSummaryVo> {

    ExistUserSummaryVo convert(int year, int month, QueryResult source);

    ExistUserSummaryVo convert(int year, int month,int day, QueryResult currentExistUser,QueryResult renewalT0User,QueryResult newUser);
}
