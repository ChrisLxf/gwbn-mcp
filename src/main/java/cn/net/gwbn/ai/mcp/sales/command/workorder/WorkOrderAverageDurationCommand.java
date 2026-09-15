package cn.net.gwbn.ai.mcp.sales.command.workorder;

import cn.net.gwbn.ai.mcp.engine.QueryBuilder;
import cn.net.gwbn.ai.mcp.engine.QueryEngine;
import cn.net.gwbn.ai.mcp.engine.ast.ConditionNode;
import cn.net.gwbn.ai.mcp.engine.model.Query;
import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderAverageDurationVo;
import cn.net.gwbn.ai.mcp.sales.converter.workorder.WorkOrderAverageDurationVoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 工单平均完成时长统计.
 *
 * @author lixiaofeng
 * @date 9/14/26
 **/
public class WorkOrderAverageDurationCommand {

    private static final Logger logger = LoggerFactory.getLogger(WorkOrderAverageDurationCommand.class);

    private static final String METRIC = "metric_value";

    private final QueryEngine queryEngine;

    private final WorkOrderAverageDurationVoConverter converter;

    public WorkOrderAverageDurationCommand(QueryEngine queryEngine, WorkOrderAverageDurationVoConverter converter) {
        this.queryEngine = queryEngine;
        this.converter = converter;
    }

    /**
     * 工单平均完成时长统计.
     * <p>
     * 同时统计:
     * <p>
     * 1. 当天平均完成时长
     * 2. 当月平均完成时长
     *
     * @param year      年
     * @param month     月
     * @param day       日
     * @param cityNames 城市名称
     * @return 平均完成时长统计结果
     */
    public WorkOrderAverageDurationVo statistic(int year, int month, int day, List<String> cityNames) {

        /*
         * 查询当天数据.
         */
        Query dayQuery = buildQuery(year, month, day, cityNames);

        QueryResult dayResult = queryEngine.query(dayQuery);

        logger.debug("average duration day columns={}", dayResult.getColumns());

        logger.debug("average duration day rows={}", dayResult.getRows());

        /*
         * 查询当月数据.
         */
        Query monthQuery = buildQuery(year, month, 0, cityNames);

        QueryResult monthResult = queryEngine.query(monthQuery);

        logger.debug("average duration month columns={}", monthResult.getColumns());

        logger.debug("average duration month rows={}", monthResult.getRows());

        return converter.convert(year, month, day, dayResult, monthResult);
    }

    /**
     * 构建平均完成时长查询.
     */
    private Query buildQuery(int year, int month, int day, List<String> cityNames) {

        QueryBuilder builder = QueryBuilder.table("work_order_statistic s")
                .dimension("s.order_type")
                .dimension("s.city_id")
                .dimension("s.city_name");

        List<ConditionNode> conditions = new ArrayList<>();

        conditions.add(QueryBuilder.eq("s.create_year", year));

        conditions.add(QueryBuilder.eq("s.create_month", month));

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

        /*
         * 工单状态.
         *
         * 平均完成时长原则上只统计已完成工单.
         */
        conditions.add(QueryBuilder.eq("s.order_state", "COMPLETED"));


        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));

        /*
         * 平均完成时长.
         *
         * 假设 duration 字段保存工单完成时长.
         */
        builder.avg("s.duration", METRIC);

        return builder.build();
    }
}