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
     * @param year      年
     * @param month     月
     * @param day       日
     * @param metric    统计指标
     * @param cityNames 城市名称, 可为空
     * @return 工单统计结果
     */
    public WorkOrderStatisticVo statistic(int year, int month, int day, String metric, List<String> cityNames, int finishType) {

        WorkOrderMetric workOrderMetric = WorkOrderMetric.from(metric);

        // 查询当天数据.
        Query dayQuery = buildQuery(year, month, day, workOrderMetric, cityNames, finishType);

        QueryResult dayResult = queryEngine.query(dayQuery);

        logger.debug("day columns={}", dayResult.getColumns());
        logger.debug("day rows={}", dayResult.getRows());

        // 查询当月数据. day = 0 表示不限制日期.
        Query monthQuery = buildQuery(year, month, 0, workOrderMetric, cityNames, finishType);

        QueryResult monthResult = queryEngine.query(monthQuery);

        logger.debug("month columns={}", monthResult.getColumns());
        logger.debug("month rows={}", monthResult.getRows());

        // 将当天和当月结果合并.
        return workOrderStatisticVoConverter.convert(year, month, day, workOrderMetric.name(), dayResult, monthResult);
    }

    /**
     * 工单完成率统计.
     *
     * @param year      年
     * @param month     月
     * @param day       日
     * @param cityNames 城市名称, 可为空
     * @return 工单完成率统计结果
     */
    public WorkOrderCompletionRateVo completionRate(int year, int month, int day, List<String> cityNames) {

        // 当日已完成
        Query dayCompletedQuery = buildQuery(year, month, day, WorkOrderMetric.COUNT, cityNames, 2);

        QueryResult dayCompletedResult = queryEngine.query(dayCompletedQuery);

        logger.debug("day completed columns={}", dayCompletedResult.getColumns());
        logger.debug("day completed rows={}", dayCompletedResult.getRows());

        // 当日全部
        Query dayTotalQuery = buildQuery(year, month, day, WorkOrderMetric.COUNT, cityNames, 0);

        QueryResult dayTotalResult = queryEngine.query(dayTotalQuery);

        logger.debug("day total columns={}", dayTotalResult.getColumns());
        logger.debug("day total rows={}", dayTotalResult.getRows());

        // 当月已完成day = 0 表示整月
        Query monthCompletedQuery = buildQuery(year, month, 0, WorkOrderMetric.COUNT, cityNames, 2);

        QueryResult monthCompletedResult = queryEngine.query(monthCompletedQuery);

        logger.debug("month completed columns={}", monthCompletedResult.getColumns());
        logger.debug("month completed rows={}", monthCompletedResult.getRows());

        // 当月全部
        Query monthTotalQuery = buildQuery(year, month, 0, WorkOrderMetric.COUNT, cityNames, 0);

        QueryResult monthTotalResult = queryEngine.query(monthTotalQuery);

        logger.debug("month total columns={}", monthTotalResult.getColumns());
        logger.debug("month total rows={}", monthTotalResult.getRows());


        /*
         * ============================
         * 转换结果
         * ============================
         */
        return workOrderCompletionRateVoConverter.convert(year, month, day, dayCompletedResult, dayTotalResult, monthCompletedResult, monthTotalResult);
    }

    /**
     * 构建工单统计查询.
     *
     * @param year      年
     * @param month     月
     * @param day       日.
     *                  day > 0 时统计指定日期.
     *                  day = 0 时统计整月.
     * @param metric    统计指标
     * @param cityNames 城市
     * @return 查询对象
     */
    private Query buildQuery(int year, int month, int day, WorkOrderMetric metric, List<String> cityNames, int finishType) {

        QueryBuilder builder = QueryBuilder.table("work_order_statistic s")
                .dimension("s.order_type")
                .dimension("s.city_id")
                .dimension("s.city_name");

        List<ConditionNode> conditions = new ArrayList<>();

        /*
         * 年
         */
        conditions.add(QueryBuilder.eq("s.create_year", year));

        /*
         * 月
         */
        conditions.add(QueryBuilder.eq("s.create_month", month));

        /*
         * 日.
         *
         * day > 0:
         * 查询指定日期.
         *
         * day = 0:
         * 不增加日期条件, 查询整月.
         */
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
        } else if (finishType == 1) {
            conditions.add(QueryBuilder.eq("s.order_state", "RUNNING"));
        }

        /*
         * WHERE
         */
        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));


        /*
         * 聚合.
         */
        aggregate(builder, metric);

        return builder.build();
    }

    /**
     * 聚合.
     */
    private void aggregate(QueryBuilder builder, WorkOrderMetric metric) {

        switch (metric) {

            case COUNT:

                /*
                 * 工单数量.
                 */
                builder.count(
                        "s.order_id",
                        METRIC
                );
                break;

            default:
                throw new IllegalArgumentException("Unsupported metric: " + metric);
        }
    }
}