package cn.net.gwbn.ai.mcp.sales.converter;

import cn.net.gwbn.ai.mcp.sales.api.SummaryVieObject;
import cn.net.gwbn.ai.mcp.engine.result.ColumnMeta;
import cn.net.gwbn.ai.mcp.engine.result.QueryResult;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lixiaofeng
 * @date 6/28/26 AM10:47
 **/
public abstract class BaseSummaryVoConverter<T extends SummaryVieObject> extends AbstractConverter<QueryResult, T> implements SummaryVoConverter<T> {

    protected Map<String, Integer> getColumnIndexMap(List<ColumnMeta> columns) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            map.put(columns.get(i).getName().toLowerCase(), i);
        }
        return map;
    }

    protected String getString(List<Object> row, Map<String, Integer> map, String... possibleNames) {
        for (String name : possibleNames) {
            Integer idx = map.get(name.toLowerCase());
            if (idx != null && row.get(idx) != null) {
                return row.get(idx).toString();
            }
        }
        return null;
    }

    protected int getInt(List<Object> row, Map<String, Integer> map, String... possibleNames) {
        for (String name : possibleNames) {
            Integer idx = map.get(name.toLowerCase());
            if (idx != null && row.get(idx) instanceof Number) {
                return ((Number) row.get(idx)).intValue();
            }
        }
        return 0;
    }

    protected long getLong(List<Object> row, Map<String, Integer> map, String... possibleNames) {

        for (String name : possibleNames) {
            Integer idx = map.get(name.toLowerCase());
            if (idx != null && row.get(idx) instanceof Number) {
                return ((Number) row.get(idx)).longValue();
            }
        }

        return 0L;
    }


    /**
     * 获取BigDecimal类型的列值.
     *
     * @param row 数据行
     * @param map 列名和下标映射
     * @param possibleNames 可能的列名
     * @return BigDecimal值, 不存在或无法转换时返回0
     */
    protected BigDecimal getBigDecimal(List<Object> row, Map<String, Integer> map, String... possibleNames) {

        for (String name : possibleNames) {

            Integer idx = map.get(name.toLowerCase());

            if (idx == null || idx < 0 || idx >= row.size()) {
                continue;
            }

            Object value = row.get(idx);

            if (value == null) {
                continue;
            }

            if (value instanceof BigDecimal) {
                return (BigDecimal) value;
            }

            // Number类型统一转换
            if (value instanceof Number) {
                return new BigDecimal(value.toString());
            }

            // 其他类型尝试按照字符串转换.
            try {
                return new BigDecimal(value.toString());
            } catch (NumberFormatException e) {
                return BigDecimal.ZERO;
            }
        }

        return BigDecimal.ZERO;
    }
}
