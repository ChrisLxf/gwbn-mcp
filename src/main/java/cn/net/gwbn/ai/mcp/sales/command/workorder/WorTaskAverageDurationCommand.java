package cn.net.gwbn.ai.mcp.sales.command.workorder;

import cn.net.gwbn.ai.mcp.engine.QueryBuilder;
import cn.net.gwbn.ai.mcp.engine.QueryEngine;
import cn.net.gwbn.ai.mcp.engine.ast.ConditionNode;
import cn.net.gwbn.ai.mcp.engine.model.Query;
import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorTaskAverageDurationVo;
import cn.net.gwbn.ai.mcp.sales.converter.workorder.WorTaskAverageDurationVoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 工单任务平均完成时长统计.
 *
 * @author lixiaofeng
 * @date 9/15/26 AM10:55
 */
public class WorTaskAverageDurationCommand {

    private static final Logger logger = LoggerFactory.getLogger(WorTaskAverageDurationCommand.class);

    /**
     * 聚合结果字段.
     */
    private static final String METRIC = "metric_value";

    /**
     * 已完成.
     */
    private static final String COMPLETED = "COMPLETED";

    private final QueryEngine queryEngine;

    private final WorTaskAverageDurationVoConverter converter;

    public WorTaskAverageDurationCommand(QueryEngine queryEngine, WorTaskAverageDurationVoConverter converter) {
        this.queryEngine = queryEngine;
        this.converter = converter;
    }

    /**
     * 查询工单任务平均完成时长.
     *
     * @param year      年
     * @param month     月
     * @param day       日, day > 0 查询指定日期, day = 0 查询整月
     * @param orderType 工单类型, null 或空表示全部
     * @param cityNames 城市名称列表
     * @param taskNames 任务名称列表
     * @return 工单任务平均完成时长
     */
    public WorTaskAverageDurationVo statistic(int year, int month, int day, String orderType, List<String> cityNames, List<String> taskNames) {

        /*
         * 查询当天.
         */
        Query dayQuery = buildQuery(year, month, day, orderType, cityNames, taskNames);

        QueryResult dayResult = queryEngine.query(dayQuery);

        /*
         * 查询当月.
         */
        Query monthQuery = buildQuery(year, month, 0, orderType, cityNames, taskNames);

        QueryResult monthResult = queryEngine.query(monthQuery);

        /*
         * 转换结果.
         */
        return converter.convert(year, month, day, dayResult, monthResult);
    }

    /**
     * 构建统计查询.
     *
     * @param year      年
     * @param month     月
     * @param day       日
     * @param orderType 工单类型
     * @param cityNames 城市名称列表
     * @param taskNames 任务名称列表
     * @return 查询对象
     */
    public Query buildQuery(int year, int month, int day, String orderType, List<String> cityNames, List<String> taskNames) {

        QueryBuilder builder = QueryBuilder.table("work_task_statistic s")
                        .dimension("s.order_type")
                        .dimension("s.city_id")
                        .dimension("s.city_name")
                        .dimension("s.task_name");

        List<ConditionNode> conditions = new ArrayList<>();

        /*
         * 年.
         */
        conditions.add(QueryBuilder.eq("s.create_year", year));

        /*
         * 月.
         */
        conditions.add(QueryBuilder.eq("s.create_month", month));

        /*
         * 日.
         *
         * day > 0:
         * 查询指定日期.
         *
         * day = 0:
         * 查询整月.
         */
        if (day > 0) {
            conditions.add(QueryBuilder.eq("s.create_day", day));
        }

        /*
         * 工单类型.
         */
        if (orderType != null && !orderType.trim().isEmpty()) {
            conditions.add(QueryBuilder.eq("s.order_type", orderType));
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

        /*
         * 任务过滤.
         */
        if (taskNames != null && !taskNames.isEmpty()) {

            if (taskNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.task_name", taskNames.get(0)));

            } else {
                conditions.add(QueryBuilder.in("s.task_name", taskNames));
            }
        }

        /*
         * 只统计已完成任务.
         */
        conditions.add(QueryBuilder.eq("s.task_state", COMPLETED));

        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));

        /*
         * 平均完成时长.
         *
         * duration 单位: 毫秒.
         */
        builder.avg("s.duration", METRIC);

        return builder.build();
    }
}