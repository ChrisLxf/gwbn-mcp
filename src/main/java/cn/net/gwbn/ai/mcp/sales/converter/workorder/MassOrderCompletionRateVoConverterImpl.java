package cn.net.gwbn.ai.mcp.sales.converter.workorder;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.MassOrderCompletionRateItemVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.MassOrderCompletionRateVo;
import cn.net.gwbn.ai.mcp.sales.converter.BaseSummaryVoConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 非单完成率转换器实现.
 *
 * @author lixiaofeng
 * @date 9/15/26
 **/
public class MassOrderCompletionRateVoConverterImpl extends BaseSummaryVoConverter<MassOrderCompletionRateVo> implements MassOrderCompletionRateVoConverter {

    private static final String FAULT_TYPE_COLUMN = "fault_type";

    private static final String CITY_ID_COLUMN = "city_id";

    private static final String CITY_NAME_COLUMN = "city_name";

    private static final String METRIC_COLUMN = "metric_value";

    /**
     * 完成率保留2位小数.
     */
    private static final int RATE_SCALE = 2;

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    @Override
    public MassOrderCompletionRateVo convert(int year, int month, int day, QueryResult dayCompletedResult, QueryResult dayTotalResult, QueryResult monthCompletedResult, QueryResult monthTotalResult) {

        MassOrderCompletionRateVo summary = new MassOrderCompletionRateVo();

        summary.setYear(year);
        summary.setMonth(month);
        summary.setDay(day);

        /*
         * 批量工单完成率统计维度:
         *
         * fault_type
         * city_id
         * city_name
         */
        summary.setDimension("fault_type");

        Map<String, MassOrderCompletionRateItemVo> itemMap = new LinkedHashMap<>();

        /*
         * 当日已完成.
         */
        mergeResult(itemMap, dayCompletedResult, DataType.DAY_COMPLETED);

        /*
         * 当日全部.
         */
        mergeResult(itemMap, dayTotalResult, DataType.DAY_TOTAL);

        /*
         * 当月已完成.
         */
        mergeResult(itemMap, monthCompletedResult, DataType.MONTH_COMPLETED);

        /*
         * 当月全部.
         */
        mergeResult(itemMap, monthTotalResult, DataType.MONTH_TOTAL);

        /*
         * 计算完成率.
         */
        for (MassOrderCompletionRateItemVo item : itemMap.values()) {
            item.setDayCompletionRate(calculateRate(item.getDayCompletedCount(), item.getDayTotalCount()));
            item.setMonthCompletionRate(calculateRate(item.getMonthCompletedCount(), item.getMonthTotalCount()));
        }
        summary.setItems(new ArrayList<>(itemMap.values()));

        return summary;
    }

    /**
     * 合并查询结果.
     */
    private void mergeResult(Map<String, MassOrderCompletionRateItemVo> itemMap, QueryResult result, DataType dataType) {

        if (result == null || result.getRows() == null || result.getRows().isEmpty()) {
            return;
        }

        Map<String, Integer> columnIndexMap = getColumnIndexMap(result.getColumns());

        for (List<Object> row : result.getRows()) {

            if (row == null || row.isEmpty()) {
                continue;
            }

            String faultType = getString(row, columnIndexMap, FAULT_TYPE_COLUMN);
            String cityId = getString(row, columnIndexMap, CITY_ID_COLUMN);
            String cityName = getString(row, columnIndexMap, CITY_NAME_COLUMN);
            String key = buildKey(faultType, cityId, cityName);

            MassOrderCompletionRateItemVo item = itemMap.get(key);

            if (item == null) {

                item = new MassOrderCompletionRateItemVo();

                item.setFaultType(faultType);
                item.setCityId(cityId);
                item.setCityName(cityName);

                itemMap.put(key, item);
            }

            int count = getInt(row, columnIndexMap, METRIC_COLUMN);

            setCount(item, dataType, count);
        }
    }

    /**
     * 设置数量.
     */
    private void setCount(MassOrderCompletionRateItemVo item, DataType dataType, int count) {

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
                throw new IllegalArgumentException("Unsupported data type: " + dataType);
        }
    }

    /**
     * 计算完成率.
     * <p>
     * 完成率 = 已完成数量 / 全部数量 * 100%.
     */
    private BigDecimal calculateRate(int completedCount, int totalCount) {

        if (totalCount <= 0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(completedCount)
                .multiply(HUNDRED)
                .divide(BigDecimal.valueOf(totalCount), RATE_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 构建数据合并Key.
     */
    private String buildKey(String faultType, String cityId, String cityName) {

        return String.valueOf(faultType) + "|" + String.valueOf(cityId) + "|" + String.valueOf(cityName);
    }

    /**
     * 单结果转换.
     */
    @Override
    public MassOrderCompletionRateVo convert(QueryResult source) {

        MassOrderCompletionRateVo summary = new MassOrderCompletionRateVo();

        Map<String, MassOrderCompletionRateItemVo> itemMap = new LinkedHashMap<>();

        mergeResult(itemMap, source, DataType.DAY_TOTAL);

        for (MassOrderCompletionRateItemVo item : itemMap.values()) {
            item.setDayCompletionRate(calculateRate(item.getDayCompletedCount(), item.getDayTotalCount()));
        }

        summary.setItems(new ArrayList<>(itemMap.values()));

        return summary;
    }

    /**
     * 数据类型.
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