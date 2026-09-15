package cn.net.gwbn.ai.mcp.sales.converter.workorder;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderAverageDurationItemVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderAverageDurationVo;
import cn.net.gwbn.ai.mcp.sales.converter.BaseSummaryVoConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 工单平均完成时长转换器实现.
 *
 * <p>
 * 原始 duration 单位: 毫秒.
 * </p>
 *
 * <p>
 * 返回单位: 分钟.
 * </p>
 *
 * @author lixiaofeng
 * @date 9/14/26
 */
public class WorkOrderAverageDurationVoConverterImpl
        extends BaseSummaryVoConverter<WorkOrderAverageDurationVo>
        implements WorkOrderAverageDurationVoConverter {

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
     * 聚合结果.
     */
    private static final String METRIC_COLUMN = "metric_value";

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
    public WorkOrderAverageDurationVo convert(
            int year,
            int month,
            int day,
            QueryResult dayResult,
            QueryResult monthResult) {

        WorkOrderAverageDurationVo summary =
                new WorkOrderAverageDurationVo();

        /*
         * 设置统计日期.
         */
        summary.setYear(year);
        summary.setMonth(month);
        summary.setDay(day);

        /*
         * 按工单类型 + 城市合并当天和当月数据.
         */
        Map<String, WorkOrderAverageDurationItemVo> itemMap =
                new LinkedHashMap<>();

        /*
         * 处理当天数据.
         */
        mergeResult(
                itemMap,
                dayResult,
                true
        );

        /*
         * 处理当月数据.
         */
        mergeResult(
                itemMap,
                monthResult,
                false
        );

        /*
         * 设置明细.
         */
        summary.setItems(
                new ArrayList<>(itemMap.values())
        );

        return summary;
    }

    /**
     * 合并查询结果.
     *
     * @param itemMap 统计结果
     * @param result 查询结果
     * @param dayData true = 当天, false = 当月
     */
    private void mergeResult(
            Map<String, WorkOrderAverageDurationItemVo> itemMap,
            QueryResult result,
            boolean dayData) {

        if (result == null
                || result.getRows() == null
                || result.getRows().isEmpty()) {
            return;
        }

        /*
         * 建立列名 -> 下标映射.
         */
        Map<String, Integer> colIndex =
                getColumnIndexMap(result.getColumns());

        for (List<Object> row : result.getRows()) {

            if (row == null || row.isEmpty()) {
                continue;
            }

            /*
             * 获取统计维度.
             */
            String orderType =
                    getString(
                            row,
                            colIndex,
                            ORDER_TYPE_COLUMN
                    );

            String cityId =
                    getString(
                            row,
                            colIndex,
                            CITY_ID_COLUMN
                    );

            String cityName =
                    getString(
                            row,
                            colIndex,
                            CITY_NAME_COLUMN
                    );

            /*
             * 构造唯一Key.
             */
            String key =
                    buildKey(
                            orderType,
                            cityId,
                            cityName
                    );

            /*
             * 获取或创建统计项.
             */
            WorkOrderAverageDurationItemVo item =
                    itemMap.get(key);

            if (item == null) {

                item = new WorkOrderAverageDurationItemVo();

                item.setOrderType(orderType);
                item.setCityId(cityId);
                item.setCityName(cityName);

                itemMap.put(key, item);
            }

            /*
             * 获取数据库聚合结果.
             *
             * AVG(duration) 的单位仍然是毫秒.
             */
            BigDecimal durationMillis =
                    getBigDecimal(
                            row,
                            colIndex,
                            METRIC_COLUMN
                    );

            /*
             * 毫秒 -> 分钟.
             */
            BigDecimal durationMinutes =
                    convertToMinutes(durationMillis);

            /*
             * 设置当天 / 当月平均完成时长.
             */
            if (dayData) {
                item.setDayAverageDuration(
                        durationMinutes
                );
            } else {
                item.setMonthAverageDuration(
                        durationMinutes
                );
            }
        }
    }

    /**
     * 毫秒转换为分钟.
     *
     * @param durationMillis 时长, 单位: 毫秒
     * @return 时长, 单位: 分钟
     */
    private BigDecimal convertToMinutes(
            BigDecimal durationMillis) {

        if (durationMillis == null) {
            return BigDecimal.ZERO;
        }

        return durationMillis.divide(
                MILLIS_PER_MINUTE,
                DURATION_SCALE,
                RoundingMode.HALF_UP
        );
    }

    /**
     * 构造统计维度Key.
     *
     * @param orderType 工单类型
     * @param cityId 城市ID
     * @param cityName 城市名称
     * @return 唯一Key
     */
    private String buildKey(
            String orderType,
            String cityId,
            String cityName) {

        return String.valueOf(orderType)
                + "|"
                + String.valueOf(cityId)
                + "|"
                + String.valueOf(cityName);
    }

    /**
     * 单结果转换.
     *
     * <p>
     * 单个QueryResult默认作为当天数据处理.
     * </p>
     *
     * @param source 查询结果
     * @return 工单平均完成时长
     */
    @Override
    public WorkOrderAverageDurationVo convert(
            QueryResult source) {

        WorkOrderAverageDurationVo summary =
                new WorkOrderAverageDurationVo();

        Map<String, WorkOrderAverageDurationItemVo> itemMap =
                new LinkedHashMap<>();

        /*
         * 单个结果按照当天数据处理.
         */
        mergeResult(
                itemMap,
                source,
                true
        );

        summary.setItems(
                new ArrayList<>(itemMap.values())
        );

        return summary;
    }
}