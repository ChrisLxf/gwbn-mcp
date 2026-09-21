package cn.net.gwbn.ai.mcp.sales.command.workorder;

import cn.net.gwbn.ai.mcp.engine.QueryBuilder;
import cn.net.gwbn.ai.mcp.engine.QueryEngine;
import cn.net.gwbn.ai.mcp.engine.ast.ConditionNode;
import cn.net.gwbn.ai.mcp.engine.model.Query;
import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderCompletionRateVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderStatisticVo;
import cn.net.gwbn.ai.mcp.sales.converter.workorder.WorkOrderCompletionRateVoConverter;
import cn.net.gwbn.ai.mcp.sales.converter.workorder.WorkOrderStatisticVoConverter;
import cn.net.gwbn.ai.mcp.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 工单统计
 *
 * @author lixiaofeng
 * @date 9/14/26 PM1:29
 **/
public class WorkOrderStatisticCommand {

    private static final Logger logger = LoggerFactory.getLogger(WorkOrderStatisticCommand.class);

    /**
     * 聚合结果字段
     */
    private static final String METRIC = "metric_value";

    private final QueryEngine queryEngine;

    private final WorkOrderStatisticVoConverter workOrderStatisticVoConverter;

    private final WorkOrderCompletionRateVoConverter workOrderCompletionRateVoConverter;

    public WorkOrderStatisticCommand(QueryEngine queryEngine, WorkOrderStatisticVoConverter workOrderStatisticVoConverter, WorkOrderCompletionRateVoConverter workOrderCompletionRateVoConverter) {
        this.queryEngine = queryEngine;
        this.workOrderStatisticVoConverter = workOrderStatisticVoConverter;
        this.workOrderCompletionRateVoConverter = workOrderCompletionRateVoConverter;
    }

    /**
     * 工单统计.
     *
     * @param year            年
     * @param month           月
     * @param day             日
     * @param dimension       统计维度
     * @param cityNames       城市名称, 可为空
     * @param useCompleteDate 是否是客服完成
     * @return 工单统计结果
     */
    public WorkOrderStatisticVo orderStatistic(int year, int month, int day, String dimension, List<String> cityNames, int finishType, boolean useCompleteDate, boolean isKfCompleted) {


        // 查询当天数据.
        Query dayQuery = buildQuery(year, month, day, cityNames, finishType, useCompleteDate, isKfCompleted);

        QueryResult dayResult = queryEngine.query(dayQuery);

        logger.debug("day columns={}", dayResult.getColumns());
        logger.debug("day rows={}", dayResult.getRows());

        // 查询当月数据. day = 0 表示不限制日期.
        Query monthQuery = buildQuery(year, month, 0, cityNames, finishType, useCompleteDate, isKfCompleted);

        QueryResult monthResult = queryEngine.query(monthQuery);

        logger.debug("month columns={}", monthResult.getColumns());
        logger.debug("month rows={}", monthResult.getRows());

        // 将当天和当月结果合并.
        return workOrderStatisticVoConverter.convert(year, month, day, dimension, dayResult, monthResult);
    }

    /**
     * 催单统计
     *
     * @param year
     * @param month
     * @param day
     * @param cityNames
     * @return
     */
    public WorkOrderStatisticVo remainderStatistic(int year, int month, int day, List<String> cityNames) {


        // 查询当天数据.
        Query dayQuery = buildRemainderCountQuery(year, month, day, cityNames);

        QueryResult dayResult = queryEngine.query(dayQuery);

        logger.debug("day columns={}", dayResult.getColumns());
        logger.debug("day rows={}", dayResult.getRows());

        // 查询当月数据. day = 0 表示不限制日期.
        Query monthQuery = buildRemainderCountQuery(year, month, 0, cityNames);

        QueryResult monthResult = queryEngine.query(monthQuery);

        logger.debug("month columns={}", monthResult.getColumns());
        logger.debug("month rows={}", monthResult.getRows());

        // 将当天和当月结果合并.
        return workOrderStatisticVoConverter.convert(year, month, day, "催单次数统计", dayResult, monthResult);
    }


    /**
     * 构造用户统计
     *
     * @param year
     * @param month
     * @param day
     * @param dimension
     * @param cityNames
     * @return
     */
    public WorkOrderStatisticVo orderUserStatistic(int year, int month, int day, String dimension, List<String> cityNames) {


        // 查询当天数据.
        Query dayQuery = buildUserQuery(year, month, day, cityNames);

        QueryResult dayResult = queryEngine.query(dayQuery);

        logger.debug("day columns={}", dayResult.getColumns());
        logger.debug("day rows={}", dayResult.getRows());

        // 查询当月数据. day = 0 表示不限制日期.
        Query monthQuery = buildUserQuery(year, month, 0, cityNames);

        QueryResult monthResult = queryEngine.query(monthQuery);

        logger.debug("month columns={}", monthResult.getColumns());
        logger.debug("month rows={}", monthResult.getRows());

        // 将当天和当月结果合并.
        return workOrderStatisticVoConverter.convert(year, month, day, dimension, dayResult, monthResult);
    }


    /**
     * 工单重复统计
     *
     * @param year
     * @param month
     * @param day
     * @param cityNames
     * @return
     */
    public WorkOrderStatisticVo orderDuplicate(int year, int month, int day, List<String> cityNames) {
        // 每日重复统计
        Query queryDayDuplicateQuery = buildDuplicateCountQuery(year, month, day, cityNames);
        QueryResult queryDayDuplicateResult = queryEngine.query(queryDayDuplicateQuery);

        // 每月重复统计
        Query queryMonthDuplicateQuery = buildDuplicateCountQuery(year, month, 0, cityNames);
        QueryResult queryMonthDuplicateResult = queryEngine.query(queryMonthDuplicateQuery);


        return workOrderStatisticVoConverter.convertDuplicate(year, month, day, "工单重复统计", queryDayDuplicateResult, queryMonthDuplicateResult);
    }

    /**
     * 构建工单统计查询.
     *
     * @param year      年
     * @param month     月
     * @param day       日.day > 0 时统计指定日期. day = 0 时统计整月.
     * @param cityNames 城市
     * @return 查询对象
     */
    private Query buildQuery(int year, int month, int day, List<String> cityNames, int finishType, boolean useCompleteDate, boolean isKfCompleted) {

        QueryBuilder builder = QueryBuilder.table("work_order_statistic s")
                .dimension("s.order_type")
                .dimension("s.city_id")
                .dimension("s.city_name");

        List<ConditionNode> conditions = new ArrayList<>();

        // 年
        conditions.add(QueryBuilder.eq("s.create_year", year));

        // 月
        conditions.add(QueryBuilder.eq("s.create_month", month));

        // 日
        if (day > 0) {
            conditions.add(QueryBuilder.eq("s.create_day", day));
        }

        /*
         * 城市过滤.
         */
        if (cityNames != null && !cityNames.isEmpty()) {
            if (cityNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.city_name", cityNames.get(0)));

            } else {
                conditions.add(QueryBuilder.in("s.city_name", cityNames));
            }
        }


        if (finishType == 2) {
            conditions.add(QueryBuilder.eq("s.order_state", "COMPLETED"));
            if (useCompleteDate) {
                conditions.add(QueryBuilder.eq("s.complete_year", year));
                conditions.add(QueryBuilder.eq("s.complete_month", month));
                if (day > 0) {
                    conditions.add(QueryBuilder.eq("s.complete_day", day));
                }
            }
        } else if (finishType == 1) {
            conditions.add(QueryBuilder.eq("s.order_state", "RUNNING"));
        }

        if (isKfCompleted) {
            conditions.add(QueryBuilder.eq("s.is_kf_completed", 1));
        }


        // WHERE
        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));


        // 聚合
        builder.count("s.order_no", METRIC);

        return builder.build();
    }

    /**
     * 构造催单数量查询
     */
    private Query buildRemainderCountQuery(int year, int month, int day, List<String> cityNames) {
        QueryBuilder builder = QueryBuilder.table("work_order_statistic s")
                .dimension("s.city_id")
                .dimension("s.city_name")
                .dimension("s.order_type");

        List<ConditionNode> conditions = new ArrayList<>();

        // 年
        conditions.add(QueryBuilder.eq("s.create_year", year));

        // 月
        conditions.add(QueryBuilder.eq("s.create_month", month));

        // 日
        if (day > 0) {
            conditions.add(QueryBuilder.eq("s.create_day", day));
        }

        /*
         * 城市过滤.
         */
        if (cityNames != null && !cityNames.isEmpty()) {
            if (cityNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.city_name", cityNames.get(0)));

            } else {
                conditions.add(QueryBuilder.in("s.city_name", cityNames));
            }
        }


        // WHERE
        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));

        // 聚合
        builder.sum("s.remind_times", METRIC);

        return builder.build();
    }

    /**
     * 计算工单重复
     */
    private Query buildDuplicateCountQuery(int year, int month, int day, List<String> cityNames) {
        QueryBuilder builder = QueryBuilder.table("work_order_statistic s")
                .dimension("s.city_id")
                .dimension("s.city_name")
                .dimension("s.user_id")
                .dimension("s.order_type");

        List<ConditionNode> conditions = new ArrayList<>();

        // 年
        conditions.add(QueryBuilder.eq("s.create_year", year));

        // 月
        conditions.add(QueryBuilder.eq("s.create_month", month));

        // 日
        if (day > 0) {
            conditions.add(QueryBuilder.eq("s.create_day", day));
        }

        /*
         * 城市过滤.
         */
        if (cityNames != null && !cityNames.isEmpty()) {
            if (cityNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.city_name", cityNames.get(0)));

            } else {
                conditions.add(QueryBuilder.in("s.city_name", cityNames));
            }
        }

        // WHERE
        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));

        // 聚合
        builder.count("s.user_id", METRIC);

        return builder.build();
    }


    /**
     * 构造用户数量统计
     */
    private Query buildUserQuery(int year, int month, int day, List<String> cityNames) {

        QueryBuilder builder = QueryBuilder.table("work_order_statistic s")
                .dimension("s.order_type")
                .dimension("s.city_id")
                .dimension("s.city_name");

        List<ConditionNode> conditions = new ArrayList<>();

        // 年
        conditions.add(QueryBuilder.eq("s.create_year", year));

        // 月
        conditions.add(QueryBuilder.eq("s.create_month", month));

        // 日
        if (day > 0) {
            conditions.add(QueryBuilder.eq("s.create_day", day));
        }

        /*
         * 城市过滤.
         */
        if (cityNames != null && !cityNames.isEmpty()) {
            if (cityNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.city_name", cityNames.get(0)));

            } else {
                conditions.add(QueryBuilder.in("s.city_name", cityNames));
            }
        }

        // WHERE
        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));


        // 聚合
        builder.count("s.user_id", METRIC);

        return builder.build();
    }


}