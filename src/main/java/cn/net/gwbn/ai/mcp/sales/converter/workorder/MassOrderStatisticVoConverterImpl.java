package cn.net.gwbn.ai.mcp.sales.converter.workorder;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.workorder.MassOrderStatisticItemVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.MassOrderStatisticVo;
import cn.net.gwbn.ai.mcp.sales.converter.BaseSummaryVoConverter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 非单户故障统计结果转换器.
 *
 * @author lixiaofeng
 * @date 9/14/26 PM4:20
 **/
public class MassOrderStatisticVoConverterImpl extends BaseSummaryVoConverter<MassOrderStatisticVo> implements MassOrderStatisticVoConverter {

    /**
     * 故障类型.
     */
    private static final String FAULT_TYPE_COLUMN = "fault_type";

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

    @Override
    public MassOrderStatisticVo convert(int year, int month, int day, QueryResult dayResult, QueryResult monthResult) {

        MassOrderStatisticVo summary = new MassOrderStatisticVo();

        /*
         * 设置日期信息.
         */
        summary.setYear(year);
        summary.setMonth(month);
        summary.setDay(day);

        /*
         * 使用:
         *
         * fault_type + city_id + city_name
         *
         * 作为统计维度.
         */
        Map<String, MassOrderStatisticItemVo> itemMap = new LinkedHashMap<>();

        /*
         * 合并当天数据.
         */
        mergeResult(itemMap, dayResult, true);

        /*
         * 合并当月数据.
         */
        mergeResult(itemMap, monthResult, false);

        /*
         * 转换为List.
         */
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
    private void mergeResult(Map<String, MassOrderStatisticItemVo> itemMap, QueryResult result, boolean dayData) {

        if (result == null || result.getRows() == null || result.getRows().isEmpty()) {

            return;
        }

        /*
         * 获取列名 -> 下标映射.
         *
         * 使用基类方法.
         */
        Map<String, Integer> colIndex = getColumnIndexMap(result.getColumns());

        for (List<Object> row : result.getRows()) {
            if (row == null || row.isEmpty()) {
                continue;
            }

            /*
             * 获取故障类型.
             *
             * 使用基类 getString().
             */
            String faultType = getString(row, colIndex, FAULT_TYPE_COLUMN);

            /*
             * 获取城市ID.
             *
             * 使用基类 getString().
             */
            String cityId = getString(row, colIndex, CITY_ID_COLUMN);

            /*
             * 获取城市名称.
             *
             * 使用基类 getString().
             */
            String cityName = getString(row, colIndex, CITY_NAME_COLUMN);

            /*
             * 构造统计维度Key.
             */
            String key = buildKey(faultType, cityId, cityName);

            /*
             * 获取已有统计项.
             */
            MassOrderStatisticItemVo item = itemMap.get(key);

            /*
             * 如果不存在则创建.
             */
            if (item == null) {

                item = new MassOrderStatisticItemVo();

                item.setFaultType(faultType);
                item.setCityId(cityId);
                item.setCityName(cityName);

                itemMap.put(key, item);
            }

            /*
             * 获取统计数量.
             *
             * 使用基类 getInt().
             */
            int count = getInt(row, colIndex, METRIC_COLUMN);

            /*
             * 设置当天或当月数量.
             */
            if (dayData) {
                item.setDayCount(count);

            } else {

                item.setMonthCount(count);
            }
        }
    }

    /**
     * 构造统计维度Key.
     *
     * @param faultType 故障类型
     * @param cityId 城市ID
     * @param cityName 城市名称
     * @return 统计维度Key
     */
    private String buildKey(String faultType, String cityId, String cityName) {

        return String.valueOf(faultType) + "|" + String.valueOf(cityId) + "|" + String.valueOf(cityName);
    }

    /**
     * 单结果转换.
     *
     * <p>
     * 当前统计需要同时处理当天和当月两个查询结果,
     * 因此单个QueryResult无法完整构造最终结果.
     * </p>
     *
     * @param source 查询结果
     * @return 统计结果
     */
    @Override
    public MassOrderStatisticVo convert(QueryResult source) {

        MassOrderStatisticVo summary = new MassOrderStatisticVo();

        Map<String, MassOrderStatisticItemVo> itemMap = new LinkedHashMap<>();

        /*
         * 单个QueryResult默认作为当天数据处理.
         */
        mergeResult(itemMap, source, true);

        summary.setItems(new ArrayList<>(itemMap.values()));

        return summary;
    }
}