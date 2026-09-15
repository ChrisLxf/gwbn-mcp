package cn.net.gwbn.ai.mcp.sales.converter.workorder;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorTaskAverageDurationItemVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorTaskAverageDurationVo;
import cn.net.gwbn.ai.mcp.sales.converter.BaseSummaryVoConverter;

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
public class WorTaskAverageDurationVoConverterImpl extends BaseSummaryVoConverter<WorTaskAverageDurationVo> implements WorTaskAverageDurationVoConverter {

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
     * 1分钟 = 60000毫秒.
     */
    private static final BigDecimal MILLIS_PER_MINUTE = BigDecimal.valueOf(60_000L);

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
     * 合并查询结果.
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

            WorTaskAverageDurationItemVo item =
                    itemMap.get(key);

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