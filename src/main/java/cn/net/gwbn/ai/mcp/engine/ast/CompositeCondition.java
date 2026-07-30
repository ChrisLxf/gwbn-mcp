package cn.net.gwbn.ai.mcp.engine.ast;

import java.util.List;

/**
 * 组合条件
 *
 * @author lixiaofeng
 * @date 3/25/26 PM4:53
 **/
public class CompositeCondition implements ConditionNode {

    public enum Type {AND, OR}

    /**
     * 伙计类型
     */
    private Type type;

    /**
     * 条件列表
     */
    private List<ConditionNode> conditions;

    public CompositeCondition() {
    }

    public CompositeCondition(Type type, List<ConditionNode> conditions) {
        this.type = type;
        this.conditions = conditions;
    }

    public Type getType() {
        return type;
    }

    public List<ConditionNode> getConditions() {
        return conditions;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setConditions(List<ConditionNode> conditions) {
        this.conditions = conditions;
    }
}
