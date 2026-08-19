package cn.net.gwbn.ai.mcp.sales.command;

import cn.net.gwbn.ai.mcp.engine.QueryBuilder;
import cn.net.gwbn.ai.mcp.engine.QueryEngine;
import cn.net.gwbn.ai.mcp.engine.ast.ConditionNode;
import cn.net.gwbn.ai.mcp.engine.model.Query;
import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.user.ActiveUserSummaryVo;
import cn.net.gwbn.ai.mcp.sales.converter.user.ActiveUserSummaryVoConverter;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * 统计城市的活跃用户
 *
 * @author lixiaofeng
 * @date 8/19/26 PM1:38
 **/
public class ActiveUserSummaryCommand {


    private static final String METRIC = "metric_value";

    private final QueryEngine queryEngine;

    private final ActiveUserSummaryVoConverter activeUserSummaryVoConverter;

    public ActiveUserSummaryCommand(QueryEngine queryEngine, ActiveUserSummaryVoConverter activeUserSummaryVoConverter) {
        this.queryEngine = queryEngine;
        this.activeUserSummaryVoConverter = activeUserSummaryVoConverter;
    }

    public ActiveUserSummaryVo getCityActiveUserCount(List<String> cityNames, int year, int month) {
        QueryResult result = queryEngine.query(buildQuery(year, month, cityNames));
        return activeUserSummaryVoConverter.convert(result);
    }


    /**
     * 构造月初在网户数
     */
    private Query buildQuery(int year, int month, List<String> cityNames) {


        QueryBuilder builder = QueryBuilder.table("active_user_statistic s");


        // 城市维度
        builder.dimension("s.city_id");
        builder.dimension("s.city_name");

        List<ConditionNode> conditions = new ArrayList<>();


        // 计算月份
        YearMonth current = YearMonth.of(year, month);
        YearMonth start = current.minusMonths(1);

        int startSummaryDate = start.getYear() * 100 + start.getMonthValue();
        conditions.add(QueryBuilder.eq("s.summary_date", startSummaryDate));


        // 城市过滤
        if (cityNames != null && !cityNames.isEmpty()) {
            if (cityNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.city_name", cityNames.get(0)));
            } else {
                conditions.add(QueryBuilder.in("s.city_name", cityNames));
            }
        }

        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));

        /**
         * 去重统计用户
         */
        builder.count("s.bill_id", METRIC);

        return builder.build();
    }


}
