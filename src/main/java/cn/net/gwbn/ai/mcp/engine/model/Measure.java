package cn.net.gwbn.ai.mcp.engine.model;

/**
 * 度量定义（聚合指标）
 *
 * @author lixiaofeng
 * @date 3/25/26 PM4:52
 **/
public class Measure {

    /**
     * 列名
     */
    private String column;

    /**
     * 聚合函数，如 SUM / COUNT / AVG
     */
    private String function;

    /**
     * 别名
     */
    private String alias;

    public Measure(String column, String function, String alias) {
        this.column = column;
        this.function = function;
        this.alias = alias;
    }

    public String getColumn() {
        return column;
    }

    public String getFunction() {
        return function;
    }

    public String getAlias() {
        return alias;
    }
}
