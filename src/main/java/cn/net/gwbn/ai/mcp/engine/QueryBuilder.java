package cn.net.gwbn.ai.mcp.engine;



import cn.net.gwbn.ai.mcp.engine.ast.CompositeCondition;
import cn.net.gwbn.ai.mcp.engine.ast.ConditionNode;
import cn.net.gwbn.ai.mcp.engine.ast.LeafCondition;
import cn.net.gwbn.ai.mcp.engine.model.Dimension;
import cn.net.gwbn.ai.mcp.engine.model.Join;
import cn.net.gwbn.ai.mcp.engine.model.Measure;
import cn.net.gwbn.ai.mcp.engine.model.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 报表查询构造期
 *
 * @author lixiaofeng
 * @date 3/25/26 PM5:19
 **/
public class QueryBuilder {

    private final Query query = new Query();

    /**
     * 纬度定义集合
     */
    private final List<Dimension> dimensions = new ArrayList<>();

    /**
     * 度量定义集合
     */
    private final List<Measure> measures = new ArrayList<>();

    /**
     * 数据过滤定义
     */
    private ConditionNode where;

    /**
     * 聚合过滤定义
     */
    private ConditionNode having;

    // ================= 静态入口 =================

    public static QueryBuilder table(String table) {
        QueryBuilder builder = new QueryBuilder();
        builder.query.setTable(table);
        return builder;
    }

    // ================= 维度 =================

    public QueryBuilder dimension(String column) {
        dimensions.add(new Dimension(column));
        return this;
    }

    // ================= 度量 =================

    public QueryBuilder sum(String column, String alias) {
        measures.add(new Measure(column, "SUM", alias));
        return this;
    }

    public QueryBuilder count(String column, String alias) {
        measures.add(new Measure(column, "COUNT", alias));
        return this;
    }

    public QueryBuilder avg(String column, String alias) {
        measures.add(new Measure(column, "AVG", alias));
        return this;
    }

    // ================= 条件 =================

    public QueryBuilder where(ConditionNode condition) {
        this.where = condition;
        return this;
    }

    public QueryBuilder having(ConditionNode condition) {
        this.having = condition;
        return this;
    }

    // ================= join =================

    public QueryBuilder join(Join.Type type, String table, String left, String right) {
        if (query.getJoins() == null) {
            query.setJoins(new ArrayList<>());
        }
        query.getJoins().add(new Join(type, table, left, right));
        return this;
    }

    // ================= 构建 =================

    public Query build() {
        query.setDimensions(dimensions);
        query.setMeasures(measures);
        query.setWhere(where);
        query.setHaving(having);
        return query;
    }

    // ================= 条件快捷方法 =================

    public static LeafCondition eq(String column, Object value) {
        return new LeafCondition(column, "=", value);
    }

    public static LeafCondition gt(String column, Object value) {
        return new LeafCondition(column, ">", value);
    }

    public static LeafCondition gte(String column, Object value) {
        return new LeafCondition(column, ">=", value);
    }

    public static LeafCondition lt(String column, Object value) {
        return new LeafCondition(column, "<", value);
    }

    public static LeafCondition lte(String column, Object value) {
        return new LeafCondition(column, "<=", value);
    }

    public static LeafCondition like(String column, Object value) {
        return new LeafCondition(column, "LIKE", value);
    }

    public static LeafCondition in(String column, Collection<?> values) {
        return new LeafCondition(column, "IN", values);
    }


    public static CompositeCondition and(ConditionNode... nodes) {
        CompositeCondition cc = new CompositeCondition();
        cc.setType(CompositeCondition.Type.AND);
        cc.setConditions(Arrays.asList(nodes));
        return cc;
    }

    public static CompositeCondition or(ConditionNode... nodes) {
        CompositeCondition cc = new CompositeCondition();
        cc.setType(CompositeCondition.Type.OR);
        cc.setConditions(Arrays.asList(nodes));
        return cc;
    }
}
