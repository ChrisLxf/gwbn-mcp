package cn.net.gwbn.ai.mcp.sales.converter.workorder;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderStatisticItemVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderStatisticVo;
import cn.net.gwbn.ai.mcp.sales.converter.BaseSummaryVoConverter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 工单统计结果转换器.
 *
 * @author lixiaofeng
 * @date 9/14/26 PM1:53
 */
public class WorkOrderStatisticVoConverterImpl extends BaseSummaryVoConverter<WorkOrderStatisticVo> implements WorkOrderStatisticVoConverter {

    /**
     * 工单类型.
     */
    private static final String ORDER_TYPE_COLUMN = "order_type";

    /**
     * 城市 ID.
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

    @Override
    public WorkOrderStatisticVo convert(int year, int month, int day, String metric, QueryResult dayResult, QueryResult monthResult) {

        WorkOrderStatisticVo summary = new WorkOrderStatisticVo();

        /*
         * 基本信息.
         */
        summary.setYear(year);
        summary.setMonth(month);
        summary.setDay(day);

        /*
         * 当前统计维度固定为工单类型.
         */
        summary.setDimension(ORDER_TYPE_COLUMN);

        /*
         * 统计指标.
         */
        summary.setMetric(metric);

        /*
         * 使用 LinkedHashMap 保证最终结果顺序
         * 与查询结果顺序基本一致.
         */
        Map<String, WorkOrderStatisticItemVo> itemMap = new LinkedHashMap<>();

        /*
         * 处理当天数据.
         */
        buildItems(dayResult, itemMap, true);

        /*
         * 处理当月数据.
         */
        buildItems(monthResult, itemMap, false);

        /*
         * 设置最终结果.
         */
        summary.setItems(new ArrayList<>(itemMap.values()));

        return summary;
    }

    /**
     * 处理查询结果.
     *
     * @param result 查询结果
     * @param itemMap 结果 Map
     * @param isDay true = 当天, false = 当月
     */
    private void buildItems(QueryResult result, Map<String, WorkOrderStatisticItemVo> itemMap, boolean isDay) {

        if (result == null || result.getRows() == null || result.getRows().isEmpty()) {

            return;
        }

        /*
         * 建立列名 -> 下标映射.
         */
        Map<String, Integer> colIndex = getColumnIndexMap(result.getColumns());

        for (List<Object> row : result.getRows()) {

            if (row == null || row.isEmpty()) {
                continue;
            }

            /*
             * 获取维度信息.
             */
            String orderType = getString(row, colIndex, ORDER_TYPE_COLUMN);

            String cityId = getString(row, colIndex, CITY_ID_COLUMN);

            String cityName = getString(row, colIndex, CITY_NAME_COLUMN);

            /*
             * 以:
             *
             * orderType + cityId + cityName
             *
             * 作为唯一 Key.
             */
            String key = buildKey(orderType, cityId, cityName);

            WorkOrderStatisticItemVo item = itemMap.get(key);

            /*
             * 第一次出现该维度.
             */
            if (item == null) {

                item = new WorkOrderStatisticItemVo();

                item.setDimensionValue(orderType);
                item.setCityId(cityId);
                item.setCityName(cityName);

                itemMap.put(key, item);
            }

            /*
             * 获取统计值.
             *
             * 这里直接使用 BaseSummaryVoConverter
             * 提供的 getInt().
             */
            int count = getInt(row, colIndex, METRIC_COLUMN);

            /*
             * 设置当天 / 当月数量.
             */
            if (isDay) {
                item.setDayCount(count);

            } else {
                item.setMonthCount(count);
            }
        }
    }

    /**
     * 构造唯一 Key.
     */
    private String buildKey(String orderType, String cityId, String cityName) {
        return String.valueOf(orderType) + "_" + String.valueOf(cityId) + "_" + String.valueOf(cityName);
    }

    /**
     * IConverter 要求的转换方法.
     *
     * 当前工单统计需要同时使用当天和当月两个结果,
     * 因此该方法不是实际业务入口.
     *
     * @param source 查询结果
     * @return 工单统计结果
     */
    @Override
    public WorkOrderStatisticVo convert(QueryResult source) {

        WorkOrderStatisticVo summary = new WorkOrderStatisticVo();

        Map<String, WorkOrderStatisticItemVo> itemMap = new LinkedHashMap<>();

        /*
         * 单个 QueryResult 默认作为当天数据处理.
         */
        buildItems(source, itemMap, true);

        summary.setItems(new ArrayList<>(itemMap.values()));

        return summary;
    }
}