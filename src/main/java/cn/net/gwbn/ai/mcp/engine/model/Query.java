package cn.net.gwbn.ai.mcp.engine.model;


import cn.net.gwbn.ai.mcp.engine.ast.ConditionNode;

import java.util.List;

/**
 * 查询对象
 *
 * @author lixiaofeng
 * @date 3/25/26 PM4:51
 **/
public class Query {

    /**
     * 事实表
     */
    private String table;

    /**
     * 普通查询字段
     * <p>
     * SELECT xxx
     */
    private List<SelectField> fields;

    /**
     * 纬度字段
     * <p>
     * GROUP BY
     */
    private List<Dimension> dimensions;

    /**
     * 度量字段
     * <p>
     * COUNT/SUM/AVG
     */
    private List<Measure> measures;

    /**
     * 明细过滤
     */
    private ConditionNode where;

    /**
     * 聚合过滤
     */
    private ConditionNode having;

    /**
     * 关联条件
     */
    private List<Join> joins;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<Dimension> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<Dimension> dimensions) {
        this.dimensions = dimensions;
    }

    public List<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }

    public ConditionNode getWhere() {
        return where;
    }

    public void setWhere(ConditionNode where) {
        this.where = where;
    }

    public ConditionNode getHaving() {
        return having;
    }

    public void setHaving(ConditionNode having) {
        this.having = having;
    }

    public List<Join> getJoins() {
        return joins;
    }

    public void setJoins(List<Join> joins) {
        this.joins = joins;
    }

    public List<SelectField> getFields() {
        return fields;
    }

    public void setFields(List<SelectField> fields) {
        this.fields = fields;
    }
}
