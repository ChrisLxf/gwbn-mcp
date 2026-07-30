package cn.net.gwbn.ai.mcp.engine.ast;

/**
 * 基础条件节点
 *
 * @author lixiaofeng
 * @date 3/25/26 PM5:08
 **/
public class LeafCondition implements ConditionNode {

    /**
     * 字段名
     */
    private String column;

    /**
     * 操作符
     */
    private String operator;

    /**
     * 操作符
     */
    private Object value;

    public LeafCondition(String column, String operator, Object value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    public String getColumn() {
        return column;
    }

    public String getOperator() {
        return operator;
    }

    public Object getValue() {
        return value;
    }
}
