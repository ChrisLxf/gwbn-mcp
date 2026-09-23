package cn.net.gwbn.ai.mcp.sales.command.workorder;

import cn.net.gwbn.ai.mcp.engine.QueryBuilder;
import cn.net.gwbn.ai.mcp.engine.QueryEngine;
import cn.net.gwbn.ai.mcp.engine.ast.ConditionNode;
import cn.net.gwbn.ai.mcp.engine.model.Query;
import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorTaskAverageDurationVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderStatisticVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkTaskCountStatisticVo;
import cn.net.gwbn.ai.mcp.sales.converter.workorder.WorTaskAverageDurationVoConverter;
import cn.net.gwbn.ai.mcp.sales.converter.workorder.WorkOrderStatisticVoConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
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

    private static final String RUNNING = "RUNNING";

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
     * @param cityNames 城市名称列表
     * @param taskNames 任务名称列表
     * @return 工单任务平均完成时长
     */
    public WorTaskAverageDurationVo taskAverageDurationStatistic(int year, int month, int day, List<String> cityNames, List<String> taskNames) {

        // 查询当天.
        Query dayQuery = buildQuery(year, month, day, cityNames, taskNames);
        QueryResult dayResult = queryEngine.query(dayQuery);

        //  查询当月.
        Query monthQuery = buildQuery(year, month, 0, cityNames, taskNames);
        QueryResult monthResult = queryEngine.query(monthQuery);

        // 转换结果.
        return converter.convert(year, month, day, dayResult, monthResult);
    }

    /**
     * 任务无人接单统计
     */
    public WorkTaskCountStatisticVo taskNotAssignee(int year, int month, int day, List<String> cityNames) {
        // 查询当前
        Query dayQuery = buildTaskIsNotAssigneeQuery(year, month, day, cityNames);
        QueryResult dayResult = queryEngine.query(dayQuery);

        // 查询当月
        Query monthQuery = buildTaskIsNotAssigneeQuery(year, month, 0, cityNames);
        QueryResult monthResult = queryEngine.query(monthQuery);

        return converter.convert(year, month, day, "无人接单", dayResult, monthResult);
    }


    /**
     * 任务统计
     */
    public WorkTaskCountStatisticVo taskCount(int year, int month, int day, List<String> cityNames, List<String> taskNames, boolean isCompleted) {
        // 查询当前
        Query dayQuery = buildTaskQuery(year, month, day, cityNames, isCompleted, taskNames);
        QueryResult dayResult = queryEngine.query(dayQuery);

        // 查询当月
        Query monthQuery = buildTaskQuery(year, month, 0, cityNames, isCompleted, taskNames);
        QueryResult monthResult = queryEngine.query(monthQuery);

        return converter.convert(year, month, day, "无人接单", dayResult, monthResult);
    }


    /**
     * 构建平均时长统计查询.
     *
     * @param year      年
     * @param month     月
     * @param day       日
     * @param cityNames 城市名称列表
     * @param taskNames 任务名称列表
     * @return 查询对象
     */
    protected Query buildQuery(int year, int month, int day, List<String> cityNames, List<String> taskNames) {

        QueryBuilder builder = QueryBuilder.table("work_task_statistic s")
                .dimension("s.city_id")
                .dimension("s.city_name")
                .dimension("s.task_name")
                .dimension("s.order_type");

        List<ConditionNode> conditions = new ArrayList<>();

        // 年.
        conditions.add(QueryBuilder.eq("s.create_year", year));

        // 月
        conditions.add(QueryBuilder.eq("s.create_month", month));

        // 日.day > 0:查询指定日期.day = 0:查询整月.
        if (day > 0) {
            conditions.add(QueryBuilder.eq("s.create_day", day));
        }


        //城市过滤.
        if (cityNames != null && !cityNames.isEmpty()) {
            if (cityNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.city_name", cityNames.get(0)));
            } else {
                conditions.add(QueryBuilder.in("s.city_name", cityNames));
            }
        }

        // 任务过滤.
        if (taskNames != null && !taskNames.isEmpty()) {
            if (taskNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.task_name", taskNames.get(0)));
            } else {
                conditions.add(QueryBuilder.in("s.task_name", taskNames));
            }
        }

        // 只统计已完成任务
        conditions.add(QueryBuilder.eq("s.task_state", COMPLETED));

        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));

        // 平均完成时长.duration 单位: 毫秒.
        builder.avg("s.duration", METRIC);

        return builder.build();
    }


    /**
     * 构造无人接单查询
     */
    protected Query buildTaskIsNotAssigneeQuery(int year, int month, int day, List<String> cityNames) {

        QueryBuilder builder = QueryBuilder.table("work_task_statistic s")
                .dimension("s.city_id")
                .dimension("s.city_name")
                .dimension("s.task_name")
                .dimension("s.order_type");

        List<ConditionNode> conditions = new ArrayList<>();

        // 年.
        conditions.add(QueryBuilder.eq("s.create_year", year));

        // 月
        conditions.add(QueryBuilder.eq("s.create_month", month));

        // 日.day > 0:查询指定日期.day = 0:查询整月.
        if (day > 0) {
            conditions.add(QueryBuilder.eq("s.create_day", day));
        }

        //城市过滤.
        if (cityNames != null && !cityNames.isEmpty()) {
            if (cityNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.city_name", cityNames.get(0)));
            } else {
                conditions.add(QueryBuilder.in("s.city_name", cityNames));
            }
        }

        // 任务过滤.
        conditions.add(QueryBuilder.eq("s.task_name", "下派一线"));


        // 只统计已完成任务
        conditions.add(QueryBuilder.isNull("s.assignee"));

        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));

        // 统计数量
        builder.count("s.order_nr", METRIC);

        return builder.build();
    }


    /**
     * 非单一线平均时长统计
     */
    public WorTaskAverageDurationVo taskMassAverageDurationStatistic(int year, int month, int day, List<String> cityNames, List<String> taskNames) {

        // 查询当天.
        Query dayQuery = buildMassQuery(year, month, day, cityNames, taskNames);
        QueryResult dayResult = queryEngine.query(dayQuery);
        //  查询当月.
        Query monthQuery = buildMassQuery(year, month, 0, cityNames, taskNames);
        QueryResult monthResult = queryEngine.query(monthQuery);

        // 转换结果.
        return converter.convert(year, month, day, dayResult, monthResult);
    }


    /**
     * 构建非单平均时长统计查询.
     *
     * @param year      年
     * @param month     月
     * @param day       日
     * @param cityNames 城市名称列表
     * @param taskNames 任务名称列表
     * @return 查询对象
     */
    protected Query buildMassQuery(int year, int month, int day, List<String> cityNames, List<String> taskNames) {

        QueryBuilder builder = QueryBuilder.table("work_task_statistic s")
                .dimension("s.city_id")
                .dimension("s.city_name")
                .dimension("s.task_name")
                .dimension("s.order_type");

        List<ConditionNode> conditions = new ArrayList<>();

        // 年.
        conditions.add(QueryBuilder.eq("s.create_year", year));

        // 月
        conditions.add(QueryBuilder.eq("s.create_month", month));

        // 日.day > 0:查询指定日期.day = 0:查询整月.
        if (day > 0) {
            conditions.add(QueryBuilder.eq("s.create_day", day));
        }

        // 工单类型.
        conditions.add(QueryBuilder.eq("s.order_type", "非单"));

        //城市过滤.
        if (cityNames != null && !cityNames.isEmpty()) {
            if (cityNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.city_name", cityNames.get(0)));
            } else {
                conditions.add(QueryBuilder.in("s.city_name", cityNames));
            }
        }

        // 任务过滤.
        if (taskNames != null && !taskNames.isEmpty()) {
            if (taskNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.task_name", taskNames.get(0)));
            } else {
                conditions.add(QueryBuilder.in("s.task_name", taskNames));
            }
        }

        // 只统计已完成任务
        conditions.add(QueryBuilder.eq("s.task_state", COMPLETED));

        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));

        // 平均完成时长.duration 单位: 毫秒.
        builder.avg("s.duration", METRIC);

        return builder.build();
    }

    /**
     * 构建任务数量统计
     *
     * @param year
     * @param month
     * @param day
     * @param cityNames
     * @return
     */
    protected Query buildTaskQuery(int year, int month, int day, List<String> cityNames, boolean isCompleted, List<String> taskNames) {

        QueryBuilder builder = QueryBuilder.table("work_task_statistic s")
                .dimension("s.city_id")
                .dimension("s.city_name")
                .dimension("s.task_name")
                .dimension("s.order_type");

        List<ConditionNode> conditions = new ArrayList<>();

        // 年.
        conditions.add(QueryBuilder.eq("s.create_year", year));

        // 月
        conditions.add(QueryBuilder.eq("s.create_month", month));

        // 日.day > 0:查询指定日期.day = 0:查询整月.
        if (day > 0) {
            conditions.add(QueryBuilder.eq("s.create_day", day));
        }

        //城市过滤.
        if (cityNames != null && !cityNames.isEmpty()) {
            if (cityNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.city_name", cityNames.get(0)));
            } else {
                conditions.add(QueryBuilder.in("s.city_name", cityNames));
            }
        }

        // 过滤taskName
        if (taskNames != null && !taskNames.isEmpty()) {
            if (taskNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.task_name", taskNames.get(0)));
            } else {
                conditions.add(QueryBuilder.in("s.task_name", taskNames));
            }
        }

        if (isCompleted) {
            conditions.add(QueryBuilder.eq("s.task_state", COMPLETED));
        }

        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));

        // 统计数量
        builder.count("s.order_nr", METRIC);

        return builder.build();
    }



}