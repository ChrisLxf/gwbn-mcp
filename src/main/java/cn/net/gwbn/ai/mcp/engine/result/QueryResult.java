package cn.net.gwbn.ai.mcp.engine.result;

import java.util.List;

/**
 * 查询结果封装
 *
 * @author lixiaofeng
 * @date 3/25/26 PM4:54
 **/
public class QueryResult {

    /**
     * 列信息
     */
    private List<ColumnMeta> columns;

    /**
     * 行数据
     */
    private List<List<Object>> rows;

    /**
     * 总数
     */
    private long total;

    public QueryResult(List<ColumnMeta> columns, List<List<Object>> rows, long total) {
        this.columns = columns;
        this.rows = rows;
        this.total = total;
    }

    public List<ColumnMeta> getColumns() {
        return columns;
    }

    public List<List<Object>> getRows() {
        return rows;
    }

    public long getTotal() {
        return total;
    }
}