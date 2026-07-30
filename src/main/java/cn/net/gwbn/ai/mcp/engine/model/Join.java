package cn.net.gwbn.ai.mcp.engine.model;

/**
 * @author lixiaofeng
 * @date 3/25/26 PM4:52
 **/
public class Join {


    public enum Type {INNER, LEFT, RIGHT}

    /**
     * 连接类型
     */
    private Type type;

    /**
     * 连接表名
     */
    private String table;

    /**
     * 左表列
     */
    private String leftColumn;

    /**
     * 右表列
     */
    private String rightColumn;

    public Join(Type type, String table, String leftColumn, String rightColumn) {
        this.type = type;
        this.table = table;
        this.leftColumn = leftColumn;
        this.rightColumn = rightColumn;
    }

    public Type getType() {
        return type;
    }

    public String getTable() {
        return table;
    }

    public String getLeftColumn() {
        return leftColumn;
    }

    public String getRightColumn() {
        return rightColumn;
    }
}
