package cn.net.gwbn.ai.mcp.sales.command.workorder;

import cn.net.gwbn.ai.mcp.engine.QueryBuilder;
import cn.net.gwbn.ai.mcp.engine.QueryEngine;
import cn.net.gwbn.ai.mcp.engine.ast.ConditionNode;
import cn.net.gwbn.ai.mcp.engine.model.Query;
import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.MassOrderCompletionRateVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.MassOrderStatisticVo;
import cn.net.gwbn.ai.mcp.sales.converter.workorder.MassOrderCompletionRateVoConverter;
import cn.net.gwbn.ai.mcp.sales.converter.workorder.MassOrderStatisticVoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 非单数量统计
 *
 * @author lixiaofeng
 * @date 9/14/26 PM4:15
 **/
public class MassOrderStatisticCommand {

    private static final Logger logger = LoggerFactory.getLogger(MassOrderStatisticCommand.class);

    /**
     * 聚合结果字段.
     */
    private static final String METRIC = "metric_value";

    private final QueryEngine queryEngine;

    private final MassOrderStatisticVoConverter massOrderStatisticVoConverter;

    /**
     * 非单单完成率转换器.
     */
    private final MassOrderCompletionRateVoConverter massOrderCompletionRateVoConverter;

    public MassOrderStatisticCommand(QueryEngine queryEngine, MassOrderStatisticVoConverter massOrderStatisticVoConverter, MassOrderCompletionRateVoConverter massOrderCompletionRateVoConverter) {
        this.queryEngine = queryEngine;
        this.massOrderStatisticVoConverter = massOrderStatisticVoConverter;
        this.massOrderCompletionRateVoConverter = massOrderCompletionRateVoConverter;
    }

    /**
     * 批量工单统计.
     * <p>
     * 同时统计:
     * <p>
     * 1. 当天批量工单数量
     * 2. 当月批量工单数量
     * <p>
     * 统计维度:
     * <p>
     * fault_type
     * city_id
     * city_name
     *
     * @param year       年
     * @param month      月
     * @param day        日
     * @param cityNames  城市名称, 可为空
     * @param finishType 完成状态, 0: 全部 1: 进行中 2: 已完成
     * @return 非单统计结果
     */
    public MassOrderStatisticVo statistic(int year, int month, int day, List<String> cityNames, int finishType) {

        /*
         * 查询当天数据.
         */
        Query dayQuery = buildQuery(year, month, day, cityNames, finishType);

        QueryResult dayResult = queryEngine.query(dayQuery);

        logger.debug("mass order day columns={}", dayResult.getColumns());
        logger.debug("mass order day rows={}", dayResult.getRows());

        // 查询当月数据.day = 0 表示不限制日期.
        Query monthQuery = buildQuery(year, month, 0, cityNames, finishType);

        QueryResult monthResult = queryEngine.query(monthQuery);

        logger.debug("mass order month columns={}", monthResult.getColumns());
        logger.debug("mass order month rows={}", monthResult.getRows());

        // 将当天和当月结果合并.
        return massOrderStatisticVoConverter.convert(year, month, day, dayResult, monthResult);
    }

    /**
     * 查询批量工单完成率.
     * <p>
     * 完成率:
     * <p>
     * 已完成工单数量 / 全部工单数量 * 100%
     * <p>
     * 同时统计:
     * <p>
     * 1. 当日完成率
     * 2. 当月累计完成率
     *
     * @param year      年
     * @param month     月
     * @param day       日 day > 0: 查询指定日期的完成率，day = 0: 查询整月完成率
     * @param cityNames 城市名称, 可为空
     * @return 批量工单完成率统计结果
     */
    public MassOrderCompletionRateVo completionRate(int year, int month, int day, List<String> cityNames) {

        /*
         * ============================================================
         * 1. 查询当天已完成批量工单
         * ============================================================
         *
         * finishType = 2
         * order_state = COMPLETED
         */
        Query dayCompletedQuery = buildQuery(year, month, day, cityNames, 2);

        QueryResult dayCompletedResult =
                queryEngine.query(dayCompletedQuery);

        logger.debug("mass order completion rate day completed columns={}", dayCompletedResult.getColumns());

        logger.debug("mass order completion rate day completed rows={}", dayCompletedResult.getRows());

        /*
         * ============================================================
         * 2. 查询当天全部批量工单
         * ============================================================
         *
         * finishType = 0
         * 不限制 order_state
         */
        Query dayTotalQuery = buildQuery(year, month, day, cityNames, 0);

        QueryResult dayTotalResult = queryEngine.query(dayTotalQuery);

        logger.debug("mass order completion rate day total columns={}", dayTotalResult.getColumns());

        logger.debug("mass order completion rate day total rows={}", dayTotalResult.getRows());

        /*
         * ============================================================
         * 3. 查询当月已完成批量工单
         * ============================================================
         *
         * day = 0
         * 表示查询整个月.
         *
         * finishType = 2
         * order_state = COMPLETED
         */
        Query monthCompletedQuery = buildQuery(year, month, 0, cityNames, 2);

        QueryResult monthCompletedResult = queryEngine.query(monthCompletedQuery);

        logger.debug("mass order completion rate month completed columns={}", monthCompletedResult.getColumns());

        logger.debug("mass order completion rate month completed rows={}", monthCompletedResult.getRows());

        /*
         * ============================================================
         * 4. 查询当月全部批量工单
         * ============================================================
         *
         * day = 0
         * 表示查询整个月.
         *
         * finishType = 0
         * 不限制 order_state
         */
        Query monthTotalQuery = buildQuery(year, month, 0, cityNames, 0);

        QueryResult monthTotalResult = queryEngine.query(monthTotalQuery);

        logger.debug("mass order completion rate month total columns={}", monthTotalResult.getColumns());
        logger.debug("mass order completion rate month total rows={}", monthTotalResult.getRows());

        /*
         * ============================================================
         * 5. 合并四次查询结果
         * ============================================================
         *
         * Converter 负责:
         *
         * 当日:
         *   dayCompleted / dayTotal
         *
         * 当月:
         *   monthCompleted / monthTotal
         *
         * 最终计算完成率.
         */
        return massOrderCompletionRateVoConverter.convert(year, month, day, dayCompletedResult, dayTotalResult, monthCompletedResult, monthTotalResult);
    }

    /**
     * 构建非单统计查询.
     *
     * @param year       年
     * @param month      月
     * @param day        日 day > 0: 查询指定日期,day = 0: 查询整月
     * @param cityNames  城市名称
     * @param finishType 完成状态,0: 全部1: 进行中 2: 已完成
     * @return 查询对象
     */
    private Query buildQuery(int year, int month, int day, List<String> cityNames, int finishType) {

        QueryBuilder builder = QueryBuilder.table("mass_order_statistic s")
                .dimension("s.fault_type")
                .dimension("s.city_id")
                .dimension("s.city_name");

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
         * 工单状态过滤.
         *
         * 0: 全部工单
         * 1: 进行中
         * 2: 已完成
         */
        if (finishType == 2) {
            conditions.add(QueryBuilder.eq("s.order_state", "COMPLETED"));

        } else if (finishType == 1) {
            conditions.add(QueryBuilder.eq("s.order_state", "RUNNING"));
        }

        /*
         * WHERE.
         */
        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));

        /*
         * 统计工单数量.
         */
        builder.count("s.order_id", METRIC);

        return builder.build();
    }
}