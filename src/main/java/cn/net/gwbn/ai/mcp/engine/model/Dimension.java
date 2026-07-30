package cn.net.gwbn.ai.mcp.engine.model;

/**
 * 维度字段定义
 *
 * @author lixiaofeng
 * @date 3/25/26 PM4:51
 **/
public class Dimension {

    /**
     * 字段名称
     */
    private String column;

    public Dimension(String column) {
        this.column = column;
    }

    public String getColumn() {
        return column;
    }
}