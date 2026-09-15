package cn.net.gwbn.ai.mcp.sales.converter.workorder;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderCompletionRateItemVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderCompletionRateVo;
import cn.net.gwbn.ai.mcp.sales.converter.BaseSummaryVoConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 工单完成率转换器实现.
 *
 * @author lixiaofeng
 * @date 9/15/26
 */
public class WorkOrderCompletionRateVoConverterImpl
        extends BaseSummaryVoConverter<WorkOrderCompletionRateVo>
        implements WorkOrderCompletionRateVoConverter {

    private static final String ORDER_TYPE_COLUMN =
            "order_type";

    private static final String CITY_ID_COLUMN =
            "city_id";

    private static final String CITY_NAME_COLUMN =
            "city_name";

    private static final String METRIC_COLUMN =
            "metric_value";

    /**
     * 完成率保留2位小数.
     */
    private static final int RATE_SCALE = 2;

    /**
     * 百分比.
     */
    private static final BigDecimal HUNDRED =
            BigDecimal.valueOf(100);

    @Override
    public WorkOrderCompletionRateVo convert(
            int year,
            int month,
            int day,
            QueryResult dayCompletedResult,
            QueryResult dayTotalResult,
            QueryResult monthCompletedResult,
            QueryResult monthTotalResult) {

        WorkOrderCompletionRateVo summary =
                new WorkOrderCompletionRateVo();

        summary.setYear(year);
        summary.setMonth(month);
        summary.setDay(day);

        summary.setDimension("order_type");

        Map<String, WorkOrderCompletionRateItemVo> itemMap =
                new LinkedHashMap<>();

        /*
         * 当日已完成.
         */
        mergeResult(
                itemMap,
                dayCompletedResult,
                DataType.DAY_COMPLETED);

        /*
         * 当日全部.
         */
        mergeResult(
                itemMap,
                dayTotalResult,
                DataType.DAY_TOTAL);

        /*
         * 当月已完成.
         */
        mergeResult(
                itemMap,
                monthCompletedResult,
                DataType.MONTH_COMPLETED);

        /*
         * 当月全部.
         */
        mergeResult(
                itemMap,
                monthTotalResult,
                DataType.MONTH_TOTAL);

        /*
         * 计算完成率.
         */
        for (WorkOrderCompletionRateItemVo item :
                itemMap.values()) {

            item.setDayCompletionRate(
                    calculateRate(
                            item.getDayCompletedCount(),
                            item.getDayTotalCount()));

            item.setMonthCompletionRate(
                    calculateRate(
                            item.getMonthCompletedCount(),
                            item.getMonthTotalCount()));
        }

        summary.setItems(
                new ArrayList<>(
                        itemMap.values()));

        return summary;
    }

    /**
     * 合并查询结果.
     */
    private void mergeResult(
            Map<String, WorkOrderCompletionRateItemVo> itemMap,
            QueryResult result,
            DataType dataType) {

        if (result == null
                || result.getRows() == null
                || result.getRows().isEmpty()) {

            return;
        }

        Map<String, Integer> colIndex =
                getColumnIndexMap(
                        result.getColumns());

        for (List<Object> row : result.getRows()) {

            if (row == null || row.isEmpty()) {
                continue;
            }

            String orderType =
                    getString(
                            row,
                            colIndex,
                            ORDER_TYPE_COLUMN);

            String cityId =
                    getString(
                            row,
                            colIndex,
                            CITY_ID_COLUMN);

            String cityName =
                    getString(
                            row,
                            colIndex,
                            CITY_NAME_COLUMN);

            String key =
                    buildKey(
                            orderType,
                            cityId,
                            cityName);

            WorkOrderCompletionRateItemVo item =
                    itemMap.get(key);

            if (item == null) {

                item =
                        new WorkOrderCompletionRateItemVo();

                item.setOrderType(orderType);
                item.setCityId(cityId);
                item.setCityName(cityName);

                itemMap.put(key, item);
            }

            int count =
                    getInt(
                            row,
                            colIndex,
                            METRIC_COLUMN);

            setCount(
                    item,
                    dataType,
                    count);
        }
    }

    /**
     * 设置数量.
     */
    private void setCount(
            WorkOrderCompletionRateItemVo item,
            DataType dataType,
            int count) {

        switch (dataType) {

            case DAY_COMPLETED:
                item.setDayCompletedCount(count);
                break;

            case DAY_TOTAL:
                item.setDayTotalCount(count);
                break;

            case MONTH_COMPLETED:
                item.setMonthCompletedCount(count);
                break;

            case MONTH_TOTAL:
                item.setMonthTotalCount(count);
                break;

            default:
                throw new IllegalArgumentException(
                        "Unsupported data type: " + dataType);
        }
    }

    /**
     * 计算完成率.
     *
     * @param completedCount 已完成数量
     * @param totalCount     全部数量
     * @return 完成率, 单位: %
     */
    private BigDecimal calculateRate(
            int completedCount,
            int totalCount) {

        if (totalCount <= 0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(completedCount)
                .multiply(HUNDRED)
                .divide(
                        BigDecimal.valueOf(totalCount),
                        RATE_SCALE,
                        RoundingMode.HALF_UP);
    }

    /**
     * 构建唯一Key.
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

    @Override
    public WorkOrderCompletionRateVo convert(QueryResult source) {

        WorkOrderCompletionRateVo summary = new WorkOrderCompletionRateVo();

        Map<String, WorkOrderCompletionRateItemVo> itemMap = new LinkedHashMap<>();

        /*
         * 单结果默认作为全部数据.
         */
        mergeResult(itemMap, source, DataType.DAY_TOTAL);

        for (WorkOrderCompletionRateItemVo item : itemMap.values()) {
            item.setDayCompletionRate(calculateRate(item.getDayCompletedCount(), item.getDayTotalCount()));
        }

        summary.setItems(new ArrayList<>(itemMap.values()));

        return summary;
    }

    /**
     * 查询结果类型.
     */
    private enum DataType {

        /**
         * 当日已完成.
         */
        DAY_COMPLETED,

        /**
         * 当日全部.
         */
        DAY_TOTAL,

        /**
         * 当月已完成.
         */
        MONTH_COMPLETED,

        /**
         * 当月全部.
         */
        MONTH_TOTAL
    }
}