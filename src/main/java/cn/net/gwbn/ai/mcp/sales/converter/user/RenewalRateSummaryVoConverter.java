package cn.net.gwbn.ai.mcp.sales.converter.user;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.user.ExistUserSummaryVo;
import cn.net.gwbn.ai.mcp.sales.api.user.RenewalRateSummaryVo;
import cn.net.gwbn.ai.mcp.sales.converter.IConverter;

/**
 * @author lixiaofeng
 * @date 7/28/26 PM3:25
 **/
public interface RenewalRateSummaryVoConverter extends IConverter<QueryResult, RenewalRateSummaryVo> {


    /**
     *
     * @param year              年
     * @param month             月
     * @param t                 T+n周期
     * @param allRenewalResult  全部续费用户结果
     * @param renewalUserResult 已经续费用户结果
     * @return 续费率统计结果
     */
    RenewalRateSummaryVo convert(int year, int month, int t, QueryResult allRenewalResult, QueryResult renewalUserResult);
}
