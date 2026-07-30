package cn.net.gwbn.ai.mcp.sales.converter;

import cn.net.gwbn.ai.mcp.sales.api.SummaryVieObject;
import cn.net.gwbn.ai.mcp.engine.result.ColumnMeta;
import cn.net.gwbn.ai.mcp.engine.result.QueryResult;

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
}
