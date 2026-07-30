package cn.net.gwbn.ai.mcp.sales.command;

import cn.net.gwbn.ai.mcp.engine.QueryBuilder;
import cn.net.gwbn.ai.mcp.engine.QueryEngine;
import cn.net.gwbn.ai.mcp.engine.ast.ConditionNode;
import cn.net.gwbn.ai.mcp.engine.model.Query;
import cn.net.gwbn.ai.mcp.engine.result.ColumnMeta;
import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.user.RenewalRateSummaryVo;
import cn.net.gwbn.ai.mcp.sales.converter.user.RenewalRateSummaryVoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lixiaofeng
 * @date 7/28/26 PM2:40
 **/
public class RenewalCommand {

    private final static Logger logger = LoggerFactory.getLogger(RenewalCommand.class);

    private static final String METRIC = "metric_value";

    private final List<String> businessList = Arrays.asList("宽带续订", "订购基本套餐", "全业务套餐变更", "套餐变更");

    private final QueryEngine queryEngine;

    private final RenewalRateSummaryVoConverter renewalRateSummaryVoConverter;

    public RenewalCommand(QueryEngine queryEngine, RenewalRateSummaryVoConverter renewalRateSummaryVoConverter) {
        this.queryEngine = queryEngine;
        this.renewalRateSummaryVoConverter = renewalRateSummaryVoConverter;
    }


    public RenewalRateSummaryVo summary(int year, int month, int t, List<String> cityNames) {

        // 全部需要续费的用户结果
        QueryResult allRenewalUserResult = queryEngine.query(builderAllUserQuery(year, month, t, cityNames));
        logQueryResult("全部需要续费用户", allRenewalUserResult);

        // 已经续费的用户结果
        QueryResult renewalUserResult = queryEngine.query(builderRenewalQuery(year, month, t, cityNames));
        logQueryResult("已经续费用户", renewalUserResult);

        // 转换查询结果
        return renewalRateSummaryVoConverter.convert(year, month, t, allRenewalUserResult, renewalUserResult);
    }

    /**
     * 构造全部续费用户查询
     *
     * @param year
     * @param month
     * @param t
     * @param cityNames
     * @return
     */
    public Query builderAllUserQuery(int year, int month, int t, List<String> cityNames) {

        QueryBuilder builder = QueryBuilder.table("user_expire_detail s");
        builder.dimension("s.region_id");
        builder.dimension("s.region_name");

        List<ConditionNode> conditions = new ArrayList<>();

        // 起始年月
        conditions.add(QueryBuilder.eq("s.insert_year", year));
        conditions.add(QueryBuilder.eq("s.insert_month", month));

        // 计算结束年月
        YearMonth endMonth = YearMonth.of(year, month).plusMonths(t);

        conditions.add(QueryBuilder.eq("s.end_year", endMonth.getYear()));
        conditions.add(QueryBuilder.eq("s.end_month", endMonth.getMonthValue()));

        if (cityNames != null && !cityNames.isEmpty()) {
            if (cityNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.region_name", cityNames.get(0)));
            } else {
                conditions.add(QueryBuilder.in("s.region_name", cityNames));
            }
        }

        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));

        builder.count("s.bill_id", METRIC);

        return builder.build();
    }

    public Query builderRenewalQuery(int year, int month, int t, List<String> cityNames) {
        QueryBuilder builder = QueryBuilder.table("user_order_statistic s");
        builder.dimension("s.city_id");
        builder.dimension("s.city_name");

        List<ConditionNode> conditions = new ArrayList<>();
        conditions.add(QueryBuilder.in("s.business_name", businessList));
        conditions.add(QueryBuilder.eq("s.product_name", "宽带产品"));
        conditions.add(QueryBuilder.eq("s.create_year", year));
        conditions.add(QueryBuilder.eq("s.create_month", month));
        conditions.add(QueryBuilder.eq("s.advance_month", t));

        if (cityNames != null && !cityNames.isEmpty()) {
            if (cityNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.city_name", cityNames.get(0)));
            } else {
                conditions.add(QueryBuilder.in("s.city_name", cityNames));
            }
        }

        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));
        builder.count("s.bill_id", METRIC);

        return builder.build();
    }

    private void logQueryResult(String name, QueryResult result) {

        if (result == null) {
            logger.debug("{} result is null", name);
            return;
        }


        logger.debug("==============================");
        logger.debug("{} 查询结果", name);


        // 打印列
        logger.debug("columns:");
        for (ColumnMeta column : result.getColumns()) {
            logger.debug("  name={}, type={}", column.getName(), column.getType());
        }


        // 打印数据
        logger.debug("rows size={}", result.getRows().size());
        for (List<Object> row : result.getRows()) {
            logger.debug("row={}", row);
        }

        logger.debug("total={}", result.getTotal());
        logger.debug("==============================");
    }
}
