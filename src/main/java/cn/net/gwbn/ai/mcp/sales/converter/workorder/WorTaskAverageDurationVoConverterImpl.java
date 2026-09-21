package cn.net.gwbn.ai.mcp.sales.converter.workorder;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorTaskAverageDurationItemVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorTaskAverageDurationVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkTaskCountStatisticItemVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkTaskCountStatisticVo;
import cn.net.gwbn.ai.mcp.sales.converter.BaseSummaryVoConverter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 工单任务平均完成时长转换器实现.
 *
 * <p>
 * 数据库 duration 单位: 毫秒.
 * </p>
 *
 * <p>
 * 返回单位: 分钟.
 * </p>
 *
 * @author lixiaofeng
 * @date 9/15/26
 */
public class WorTaskAverageDurationVoConverterImpl
        extends BaseSummaryVoConverter<WorTaskAverageDurationVo>
        implements WorTaskAverageDurationVoConverter {

    /**
     * 工单类型.
     */
    private static final String ORDER_TYPE_COLUMN = "order_type";

    /**
     * 城市ID.
     */
    private static final String CITY_ID_COLUMN = "city_id";

    /**
     * 城市名称.
     */
    private static final String CITY_NAME_COLUMN = "city_name";

    /**
     * 任务名称.
     */
    private static final String TASK_NAME_COLUMN = "task_name";

    /**
     * 聚合结果.
     */
    private static final String METRIC_COLUMN = "metric_value";

    /**
     * 全部工单.
     */
    private static final String ALL_ORDER_TYPE = "全部工单";

    /**
     * 1分钟 = 60000毫秒.
     */
    private static final BigDecimal MILLIS_PER_MINUTE =
            BigDecimal.valueOf(60_000L);

    /**
     * 平均完成时长保留2位小数.
     */
    private static final int DURATION_SCALE = 2;

    @Override
    public WorTaskAverageDurationVo convert(int year, int month, int day, QueryResult dayResult, QueryResult monthResult) {

        WorTaskAverageDurationVo summary = new WorTaskAverageDurationVo();

        summary.setYear(year);
        summary.setMonth(month);
        summary.setDay(day);

        Map<String, WorTaskAverageDurationItemVo> itemMap = new LinkedHashMap<>();

        /*
         * 当日数据.
         */
        mergeResult(itemMap, dayResult, true);

        /*
         * 当月数据.
         */
        mergeResult(itemMap, monthResult, false);

        summary.setItems(new ArrayList<>(itemMap.values()));

        return summary;
    }

    /**
     * 转换工单任务数量统计结果.
     *
     * <p>
     * dayResult 对应当日数量.
     * monthResult 对应当月数量.
     * </p>
     *
     * @param year        年
     * @param month       月
     * @param day         日
     * @param dimension   统计维度
     * @param dayResult   当日查询结果
     * @param monthResult 当月查询结果
     * @return 工单任务数量统计结果
     */
    @Override
    public WorkTaskCountStatisticVo convert(int year, int month, int day, String dimension, QueryResult dayResult, QueryResult monthResult) {

        WorkTaskCountStatisticVo summary = new WorkTaskCountStatisticVo();

        summary.setYear(year);
        summary.setMonth(month);
        summary.setDay(day);
        summary.setDimension(dimension);

        Map<String, WorkTaskCountStatisticItemVo> itemMap = new LinkedHashMap<>();

        /*
         * 当日数据.
         */
        mergeTaskCountResult(itemMap, dayResult, true);

        /*
         * 当月数据.
         */
        mergeTaskCountResult(itemMap, monthResult, false);

        summary.setWorkTaskCountStatisticItemVoList(new ArrayList<>(itemMap.values()));

        return summary;
    }

    /**
     * 合并任务数量查询结果.
     *
     * @param itemMap 结果Map
     * @param result  查询结果
     * @param dayData 是否为当日数据
     */
    private void mergeTaskCountResult(Map<String, WorkTaskCountStatisticItemVo> itemMap, QueryResult result, boolean dayData) {

        if (result == null || result.getRows() == null || result.getRows().isEmpty()) {
            return;
        }

        Map<String, Integer> colIndex = getColumnIndexMap(result.getColumns());

        for (List<Object> row : result.getRows()) {

            if (row == null || row.isEmpty()) {
                continue;
            }

            /*
             * 工单类型.
             *
             * 当没有指定工单类型时,
             * 查询结果中没有 order_type 字段.
             */
            String orderType = getString(row, colIndex, ORDER_TYPE_COLUMN);

            if (StringUtils.isBlank(orderType)) {
                orderType = ALL_ORDER_TYPE;
            }

            /*
             * 城市ID.
             */
            String cityId = getString(row, colIndex, CITY_ID_COLUMN);

            /*
             * 城市名称.
             */
            String cityName = getString(row, colIndex, CITY_NAME_COLUMN);

            /*
             * 任务名称.
             */
            String taskName = getString(row, colIndex, TASK_NAME_COLUMN);

            /*
             * 工单类型 + 城市 + 任务
             * 唯一确定一条统计数据.
             */
            String key = buildKey(orderType, cityId, cityName, taskName);

            WorkTaskCountStatisticItemVo item = itemMap.get(key);

            if (item == null) {

                item = new WorkTaskCountStatisticItemVo();

                item.setOrderType(orderType);
                item.setCityId(cityId);
                item.setCityName(cityName);
                item.setTaskName(taskName);

                itemMap.put(key, item);
            }

            /*
             * 获取统计数量.
             *
             * COUNT 查询结果统一转换为 BigDecimal.
             */
            BigDecimal countValue = getBigDecimal(row, colIndex, METRIC_COLUMN);

            int count = countValue == null ? 0 : countValue.intValue();

            /*
             * 当日数量.
             */
            if (dayData) {
                item.setDayCount(count);
            } else {
                /*
                 * 当月数量.
                 */
                item.setMonthCount(count);
            }
        }
    }

    /**
     * 合并平均完成时长查询结果.
     *
     * @param itemMap 结果Map
     * @param result  查询结果
     * @param dayData 是否为当天数据
     */
    private void mergeResult(Map<String, WorTaskAverageDurationItemVo> itemMap, QueryResult result, boolean dayData) {

        if (result == null || result.getRows() == null || result.getRows().isEmpty()) {
            return;
        }

        Map<String, Integer> colIndex = getColumnIndexMap(result.getColumns());

        for (List<Object> row : result.getRows()) {

            if (row == null || row.isEmpty()) {
                continue;
            }

            /*
             * 工单类型.
             */
            String orderType = getString(row, colIndex, ORDER_TYPE_COLUMN);

            if (StringUtils.isBlank(orderType)) {
                orderType = ALL_ORDER_TYPE;
            }

            /*
             * 城市ID.
             */
            String cityId = getString(row, colIndex, CITY_ID_COLUMN);

            /*
             * 城市名称.
             */
            String cityName = getString(row, colIndex, CITY_NAME_COLUMN);

            /*
             * 任务名称.
             */
            String taskName = getString(row, colIndex, TASK_NAME_COLUMN);

            /*
             * orderType + city + task
             * 共同确定一条统计数据.
             */
            String key = buildKey(orderType, cityId, cityName, taskName);

            WorTaskAverageDurationItemVo item = itemMap.get(key);

            if (item == null) {

                item = new WorTaskAverageDurationItemVo();

                item.setOrderType(orderType);
                item.setCityId(cityId);
                item.setCityName(cityName);
                item.setTaskName(taskName);

                itemMap.put(key, item);
            }

            /*
             * 获取平均 duration.
             *
             * 数据库单位: 毫秒.
             */
            BigDecimal durationMillis = getBigDecimal(row, colIndex, METRIC_COLUMN);

            /*
             * 毫秒 -> 分钟.
             */
            BigDecimal durationMinutes = convertToMinutes(durationMillis);

            if (dayData) {
                item.setDayAverageDuration(durationMinutes);
            } else {
                item.setMonthAverageDuration(durationMinutes);
            }
        }
    }

    /**
     * 毫秒转换为分钟.
     *
     * @param durationMillis 毫秒
     * @return 分钟
     */
    private BigDecimal convertToMinutes(BigDecimal durationMillis) {

        if (durationMillis == null) {
            return BigDecimal.ZERO;
        }

        return durationMillis.divide(MILLIS_PER_MINUTE, DURATION_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 构建明细唯一Key.
     *
     * <p>
     * 同一个城市可能存在多个任务,
     * 同一个任务也可能存在不同工单类型.
     * </p>
     */
    private String buildKey(String orderType, String cityId, String cityName, String taskName) {

        return String.valueOf(orderType) + "|" + String.valueOf(cityId) + "|" + String.valueOf(cityName) + "|" + String.valueOf(taskName);
    }

    /**
     * 单结果转换.
     *
     * <p>
     * 主要用于兼容 SummaryVoConverter.
     * </p>
     */
    @Override
    public WorTaskAverageDurationVo convert(QueryResult source) {
        return null;
    }
}