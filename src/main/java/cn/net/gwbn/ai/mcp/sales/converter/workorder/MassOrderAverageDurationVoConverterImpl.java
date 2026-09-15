package cn.net.gwbn.ai.mcp.sales.converter.workorder;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.MassOrderAverageDurationItemVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.MassOrderAverageDurationVo;
import cn.net.gwbn.ai.mcp.sales.converter.BaseSummaryVoConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 非单户故障平均完成时长转换器实现.
 *
 * @author lixiaofeng
 * @date 9/14/26
 **/
public class MassOrderAverageDurationVoConverterImpl extends BaseSummaryVoConverter<MassOrderAverageDurationVo> implements MassOrderAverageDurationVoConverter {

    private static final String FAULT_TYPE_COLUMN = "fault_type";

    private static final String CITY_ID_COLUMN = "city_id";

    private static final String CITY_NAME_COLUMN = "city_name";

    private static final String METRIC_COLUMN = "metric_value";

    /**
     * 1分钟 = 60000毫秒.
     */
    private static final BigDecimal MILLIS_PER_MINUTE = BigDecimal.valueOf(60_000);

    @Override
    public MassOrderAverageDurationVo convert(int year, int month, int day, QueryResult dayResult, QueryResult monthResult) {

        MassOrderAverageDurationVo summary = new MassOrderAverageDurationVo();

        summary.setYear(year);
        summary.setMonth(month);
        summary.setDay(day);

        Map<String, MassOrderAverageDurationItemVo> itemMap = new LinkedHashMap<>();

        /*
         * 合并当天数据.
         */
        mergeResult(itemMap, dayResult, true);

        /*
         * 合并当月数据.
         */
        mergeResult(itemMap, monthResult, false);

        summary.setItems(new ArrayList<>(itemMap.values()));

        return summary;
    }

    /**
     * 合并查询结果.
     *
     * @param itemMap 结果Map
     * @param result 查询结果
     * @param dayData 是否为当天数据
     */
    private void mergeResult(Map<String, MassOrderAverageDurationItemVo> itemMap, QueryResult result, boolean dayData) {

        if (result == null || result.getRows() == null || result.getRows().isEmpty()) {
            return;
        }

        Map<String, Integer> colIndex = getColumnIndexMap(result.getColumns());

        for (List<Object> row : result.getRows()) {

            if (row == null || row.isEmpty()) {
                continue;
            }

            /*
             * 使用基类提供的 getString 方法.
             */
            String faultType = getString(row, colIndex, FAULT_TYPE_COLUMN);

            String cityId = getString(row, colIndex, CITY_ID_COLUMN);

            String cityName = getString(row, colIndex, CITY_NAME_COLUMN);
            // fault_type + city_id + city_name作为统计维度.
            String key = buildKey(faultType, cityId, cityName);

            MassOrderAverageDurationItemVo item = itemMap.get(key);

            if (item == null) {
                item = new MassOrderAverageDurationItemVo();

                item.setFaultType(faultType);
                item.setCityId(cityId);
                item.setCityName(cityName);

                itemMap.put(key, item);
            }

            /*
             * 数据库中的 duration 单位为毫秒.
             *
             * AVG(duration) 的结果也是毫秒.
             *
             * 这里统一转换成分钟.
             */
            BigDecimal averageDuration = getAverageDurationMinutes(row, colIndex);

            if (dayData) {
                item.setDayAverageDuration(averageDuration);

            } else {
                item.setMonthAverageDuration(averageDuration);
            }
        }
    }

    /**
     * 构造统计维度Key.
     */
    private String buildKey(String faultType, String cityId, String cityName) {
        return String.valueOf(faultType) + "|" + String.valueOf(cityId) + "|" + String.valueOf(cityName);
    }

    /**
     * 获取平均完成时长.
     *
     * 数据库 duration 单位:
     * 毫秒.
     *
     * 返回:
     * 分钟.
     *
     * 保留2位小数.
     */
    private BigDecimal getAverageDurationMinutes(List<Object> row, Map<String, Integer> colIndex) {

        Integer index = colIndex.get(METRIC_COLUMN);

        if (index == null || index < 0 || index >= row.size()) {
            return BigDecimal.ZERO;
        }

        Object value = row.get(index);

        if (value == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal milliseconds;

        if (value instanceof BigDecimal) {

            milliseconds = (BigDecimal) value;

        } else if (value instanceof Number) {
            milliseconds = BigDecimal.valueOf(((Number) value).doubleValue());

        } else {
            try {
                milliseconds = new BigDecimal(value.toString());
            } catch (NumberFormatException e) {
                return BigDecimal.ZERO;
            }
        }

        /*
         * 毫秒 -> 分钟.
         */
        return milliseconds.divide(MILLIS_PER_MINUTE, 2, RoundingMode.HALF_UP);
    }


    @Override public MassOrderAverageDurationVo convert(QueryResult source) {
        return  null;
    }
}