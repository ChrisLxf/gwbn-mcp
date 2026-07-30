package cn.net.gwbn.ai.mcp.sales.command;

import cn.net.gwbn.ai.mcp.engine.QueryBuilder;
import cn.net.gwbn.ai.mcp.engine.QueryEngine;
import cn.net.gwbn.ai.mcp.engine.ast.ConditionNode;
import cn.net.gwbn.ai.mcp.engine.model.Query;
import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.user.ExistUserSummaryVo;
import cn.net.gwbn.ai.mcp.sales.converter.user.ExistUserSummaryVoConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 在网用户统计
 *
 * @author lixiaofeng
 * @date 7/28/26 PM1:34
 **/
public class ExistUserSummaryCommand {


    private static final String METRIC = "metric_value";

    private final List<String> businessList = Arrays.asList("宽带续订", "订购基本套餐", "全业务套餐变更", "套餐变更");


    private final QueryEngine queryEngine;


    private final ExistUserSummaryVoConverter existUserSummaryVoConverter;

    public ExistUserSummaryCommand(QueryEngine queryEngine, ExistUserSummaryVoConverter existUserSummaryVoConverter) {
        this.queryEngine = queryEngine;
        this.existUserSummaryVoConverter = existUserSummaryVoConverter;
    }


    /**
     * 在网用户统计
     *
     * @param cityNames 城市列表
     * @param year      年
     * @param month     月
     * @return
     */
    public ExistUserSummaryVo historyExistUserSummary(List<String> cityNames, int year, int month) {
        QueryResult result = queryEngine.query(buildHistoryQuery(year, month, cityNames));
        return existUserSummaryVoConverter.convert(year, month, result);
    }

    public ExistUserSummaryVo currentExistUserSummary(int year,int month,int day,List<String> cityNames) {
        QueryResult currentExistUser = queryEngine.query(buildCurrentQuery(year, month, day, cityNames));
        QueryResult renewalT0User = queryEngine.query(builderRenewalT0(year, month, day, cityNames));
        QueryResult currentNewUser =queryEngine.query(buildCurrentNewUser(year, month, day, cityNames));
        return existUserSummaryVoConverter.convert(year, month, day,currentExistUser,renewalT0User,currentNewUser);
    }

    /**
     * 构造月初在网户数
     */
    private Query buildHistoryQuery(int year, int month, List<String> cityNames) {

        QueryBuilder builder = QueryBuilder.table("user_expire_detail s");
        builder.dimension("s.region_id");
        builder.dimension("s.region_name");

        List<ConditionNode> conditions = new ArrayList<>();
        conditions.add(QueryBuilder.eq("s.insert_year", year));
        conditions.add(QueryBuilder.eq("s.insert_month", month));


        if (cityNames != null && !cityNames.isEmpty()) {
            if (cityNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.region_name", cityNames.get(0)));
            } else {
                conditions.add(QueryBuilder.in("s.region_name", cityNames));
            }
        }

        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));

        builder.count("s.user_id", METRIC);

        return builder.build();
    }


    /**
     * 构造指定日期在网户数
     */
    private Query buildCurrentQuery(int year, int month, int day, List<String> cityNames) {

        QueryBuilder builder = QueryBuilder.table("user_expire_detail s");

        builder.dimension("s.region_id");
        builder.dimension("s.region_name");


        List<ConditionNode> conditions = new ArrayList<>();


        // 查询对应月份的月初快照
        conditions.add(QueryBuilder.eq("s.insert_year", year));
        conditions.add(QueryBuilder.eq("s.insert_month", month));


        /**
         * 截止指定日期仍未到期
         *
         * end_year > year
         * 或
         * end_year = year 且 end_month > month
         * 或
         * end_year = year 且 end_month = month 且 end_day >= day
         */
        ConditionNode expireCondition = QueryBuilder.or(QueryBuilder.gt("s.end_year", year),
                QueryBuilder.and(QueryBuilder.eq("s.end_year", year), QueryBuilder.gt("s.end_month", month)),
                QueryBuilder.and(QueryBuilder.eq("s.end_year", year), QueryBuilder.eq("s.end_month", month), QueryBuilder.gte("s.end_day", day)));


        conditions.add(expireCondition);


        // 城市过滤
        if (cityNames != null && !cityNames.isEmpty()) {
            if (cityNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.region_name", cityNames.get(0)));
            } else {
                conditions.add(QueryBuilder.in("s.region_name", cityNames));
            }
        }

        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));
        builder.count("s.user_id", METRIC);

        return builder.build();
    }

    /**
     * 查询指定日期本月到期后续费用户数
     *
     * @param year      查询年份
     * @param month     查询月份
     * @param day       查询日期
     * @param cityNames 城市
     * @return 查询
     */
    private Query builderRenewalT0(int year, int month, int day, List<String> cityNames) {

        QueryBuilder builder = QueryBuilder.table("user_order_statistic s");

        builder.dimension("s.city_id");
        builder.dimension("s.city_name");

        List<ConditionNode> conditions = new ArrayList<>();


        /**
         * 本月到期用户
         *
         * 例如查询:
         * 2026-07-30
         *
         * 只处理:
         * last_expire = 2026-07-xx
         */
        conditions.add(QueryBuilder.eq("s.last_expire_year", year));
        conditions.add(QueryBuilder.eq("s.last_expire_month", month));

        /**
         * 已经过期
         *
         * last_expire_day < 查询日期
         *
         * 例如:
         * 查询 7月30日
         *
         * 7月20日到期 -> 满足
         * 7月30日到期 -> 不满足
         */
        conditions.add(QueryBuilder.lt("s.last_expire_day", day));


        // 本月产生续费订单 防止统计未来月份或历史月份续费
        conditions.add(QueryBuilder.eq("s.create_year", year));
        conditions.add(QueryBuilder.eq("s.create_month", month));

        // 是续费产品
        conditions.add(QueryBuilder.in("s.business_name", businessList));
        conditions.add(QueryBuilder.eq("s.product_name", "宽带产品"));

        // 非提前续费
        conditions.add(QueryBuilder.eq("s.advance_month", 0));


        // 城市范围
        if (cityNames != null && !cityNames.isEmpty()) {

            if (cityNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.city_id", cityNames.get(0)));
            } else {
                conditions.add(QueryBuilder.in("s.city_name", cityNames));
            }
        }

        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));


        // 统计续费用户数
        builder.count("s.bill_id", METRIC);

        return builder.build();
    }

    private Query buildCurrentNewUser(int year, int month, int day, List<String> cityNames) {
        QueryBuilder builder = QueryBuilder.table("user_order_statistic s");

        builder.dimension("s.city_id");
        builder.dimension("s.city_name");

        List<ConditionNode> conditions = new ArrayList<>();

        // 新装产品
        conditions.add(QueryBuilder.eq("s.business_name", "宽带新装"));

        // 本月新装
        conditions.add(QueryBuilder.eq("s.create_year", year));
        conditions.add(QueryBuilder.eq("s.create_month", month));

        // 城市范围
        if (cityNames != null && !cityNames.isEmpty()) {
            if (cityNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.city_id", cityNames.get(0)));
            } else {
                conditions.add(QueryBuilder.in("s.city_name", cityNames));
            }
        }

        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));


        // 统计续费用户数
        builder.count("s.bill_id", METRIC);
        return builder.build();
    }

}
