package cn.net.gwbn.ai.mcp.engine;


import cn.net.gwbn.ai.mcp.engine.ast.CompositeCondition;
import cn.net.gwbn.ai.mcp.engine.ast.ConditionNode;
import cn.net.gwbn.ai.mcp.engine.ast.LeafCondition;
import cn.net.gwbn.ai.mcp.engine.model.Dimension;
import cn.net.gwbn.ai.mcp.engine.model.Join;
import cn.net.gwbn.ai.mcp.engine.model.Measure;
import cn.net.gwbn.ai.mcp.engine.model.Query;
import cn.net.gwbn.ai.mcp.engine.result.ColumnMeta;
import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

/**
 * @author lixiaofeng
 * @date 3/25/26 PM4:55
 **/
public class QueryEngine {

    /**
     * Spring JDBC 查询工具
     * 用于执行 SQL 并返回结果
     */
    private final JdbcTemplate jdbcTemplate;

    public
    QueryEngine(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 查询入口方法
     * <p>
     * 执行流程：
     * 1. DSL -> SQL
     * 2. SQL -> 数据库查询
     * 3. 结果 -> QueryResult
     */
    public QueryResult query(Query query) {

        // 将 Query DSL 转换为 SQL
        String sql = buildSql(query);

        System.out.println("query sql:" + sql);

        // 执行 SQL 并转换结果
        return execute(sql);
    }

    /**
     * 执行 SQL 并封装结果
     *
     * @param sql 完整 SQL
     * @return QueryResult 结构化结果
     */
    private QueryResult execute(String sql) {

        // 执行 SQL，返回 List<Map>
        // 每一行是一个 Map：key=列名, value=值
        List<Map<String, Object>> raw = jdbcTemplate.queryForList(sql);

        // 如果没有数据，直接返回空结果
        if (raw.isEmpty()) {
            return new QueryResult(Collections.emptyList(), Collections.emptyList(), 0);
        }

        // 构建列元信息（字段名 + 类型）
        List<ColumnMeta> columns = buildColumns(raw.get(0));

        // 构建数据行
        List<List<Object>> rows = new ArrayList<>();

        // 遍历每一行数据
        for (Map<String, Object> row : raw) {
            // row.values() 按 Map 的 value 顺序转为 List
            rows.add(new ArrayList<>(row.values()));
        }

        // 返回结构化结果
        return new QueryResult(columns, rows, rows.size());
    }

    /**
     * 构建列元信息
     * <p>
     * 作用：
     * 从第一行数据中提取字段名 + 类型
     * <p>
     * 注意：
     * Map key 的顺序取决于 JDBC 返回顺序（通常是 SELECT 顺序）
     */
    private List<ColumnMeta> buildColumns(Map<String, Object> row) {

        List<ColumnMeta> list = new ArrayList<>();

        for (String key : row.keySet()) {
            // 根据值推断类型
            list.add(new ColumnMeta(key, inferType(row.get(key))));
        }

        return list;
    }

    /**
     * 类型推断，用于标识字段类型（用于前端展示或处理）
     */
    private String inferType(Object v) {

        if (v == null) return "NULL";

        if (v instanceof Number) return "NUMBER";

        if (v instanceof Boolean) return "BOOLEAN";

        return "STRING";
    }

    // ================= SQL BUILD =================

    /**
     * 构造sql
     */
    private String buildSql(Query query) {

        // SELECT 子句
        String select = buildSelect(query);

        // FROM + JOIN 子句
        String from = buildFrom(query);

        // WHERE 条件（如果存在）
        String where = query.getWhere() != null ? " WHERE " + buildCondition(query.getWhere()) : "";

        // GROUP BY 子句
        String groupBy = buildGroupBy(query);

        // HAVING 条件（如果存在）
        String having = query.getHaving() != null
                ? " HAVING " + buildCondition(query.getHaving())
                : "";

        // 拼接完整 SQL
        return "SELECT " + select + " FROM " + from + where + groupBy + having;
    }

    /**
     * 构建 SELECT 部分
     * <p>
     * 包含：
     * 1. 维度字段（直接 select）
     * 2. 度量字段（聚合函数）
     */
    private String buildSelect(Query query) {

        StringBuilder sb = new StringBuilder();

        // 添加维度字段
        for (Dimension d : query.getDimensions()) {
            sb.append(d.getColumn()).append(",");
        }

        // 添加聚合字段
        for (Measure m : query.getMeasures()) {

            // 例如：SUM(amount) AS total_amount
            sb.append(m.getFunction())
                    .append("(")
                    .append(m.getColumn())
                    .append(") AS ")
                    .append(m.getAlias())
                    .append(",");
        }

        // 去掉最后一个逗号
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 构建 FROM + JOIN 部分,支持多表关联
     */
    private String buildFrom(Query query) {

        StringBuilder sb = new StringBuilder(query.getTable());

        // 如果存在 join，则逐个拼接
        if (query.getJoins() != null) {

            for (Join j : query.getJoins()) {

                sb.append(" ")
                        .append(j.getType()) // INNER / LEFT / RIGHT
                        .append(" JOIN ")
                        .append(j.getTable())
                        .append(" ON ")
                        .append(j.getLeftColumn())
                        .append(" = ")
                        .append(j.getRightColumn());
            }
        }

        return sb.toString();
    }

    /**
     * 构建 GROUP BY,所有 dimension 字段必须参与 group by
     */
    private String buildGroupBy(Query query) {

        StringBuilder sb = new StringBuilder();

        for (Dimension d : query.getDimensions()) {
            sb.append(d.getColumn()).append(",");
        }

        // 如果没有维度，则不需要 group by
        if (sb.length() == 0) return "";

        // 去掉最后一个逗号
        return " GROUP BY " + sb.substring(0, sb.length() - 1);
    }

    /**
     * 递归解析 AST 条件树
     * <p>
     * 支持：
     * - 叶子条件（=, >, <, LIKE 等）
     * - 组合条件（AND / OR）
     */
    private String buildCondition(ConditionNode node) {

        // 叶子节点：单个条件
        if (node instanceof LeafCondition) {

            LeafCondition leaf = (LeafCondition) node;

            // IN 条件
            if ("IN".equalsIgnoreCase(leaf.getOperator())) {
                Object value = leaf.getValue();
                if (!(value instanceof Collection)) {
                    throw new IllegalArgumentException("IN operator requires a Collection value.");
                }
                Collection<?> values = (Collection<?>) value;
                if (values.isEmpty()) {
                    throw new IllegalArgumentException("IN values cannot be empty.");
                }
                StringBuilder sb = new StringBuilder();
                sb.append(leaf.getColumn()).append(" IN (");
                boolean first = true;
                for (Object item : values) {
                    if (!first) {
                        sb.append(",");
                    }
                    sb.append(format(item));
                    first = false;
                }
                sb.append(")");
                return sb.toString();
            }

            // 普通条件 (=、>、<、>=、<=、LIKE ...)
            return leaf.getColumn()
                    + " "
                    + leaf.getOperator()
                    + " "
                    + format(leaf.getValue());
        }

        // 组合节点：AND / OR
        if (node instanceof CompositeCondition) {

            CompositeCondition cc = (CompositeCondition) node;

            StringBuilder sb = new StringBuilder("(");

            List<ConditionNode> list = cc.getConditions();

            for (int i = 0; i < list.size(); i++) {

                // 递归解析子节点
                sb.append(buildCondition(list.get(i)));

                // 在条件之间添加 AND / OR
                if (i < list.size() - 1) {
                    sb.append(" ").append(cc.getType()).append(" ");
                }
            }

            sb.append(")");

            return sb.toString();
        }

        // 不支持的节点类型
        throw new RuntimeException("Unsupported condition");
    }

    /**
     * 值格式化
     * SQL 拼接规则：
     * - String 类型需要加引号
     * - 数值类型直接拼接
     */
    private String format(Object v) {

        if (v instanceof String) {
            return "'" + v + "'";
        }

        return v.toString();
    }
}