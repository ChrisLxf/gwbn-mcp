package cn.net.gwbn.ai.mcp.engine;



import cn.net.gwbn.ai.mcp.engine.ast.CompositeCondition;
import cn.net.gwbn.ai.mcp.engine.ast.ConditionNode;
import cn.net.gwbn.ai.mcp.engine.ast.LeafCondition;
import cn.net.gwbn.ai.mcp.engine.model.*;

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
     * 普通字段
     */
    private final List<SelectField> fields = new ArrayList<>();


    /**
     * 维度
     */
    private final List<Dimension> dimensions = new ArrayList<>();


    /**
     * 指标
     */
    private final List<Measure> measures = new ArrayList<>();


    private ConditionNode where;


    private ConditionNode having;



    public static QueryBuilder table(String table){
        QueryBuilder builder = new QueryBuilder();
        builder.query.setTable(table);
        return builder;
    }



    /**
     * 普通select字段
     */
    public QueryBuilder select(String column){
        fields.add(new SelectField(column));
        return this;
    }



    public QueryBuilder dimension(String column){
        dimensions.add(new Dimension(column));
        return this;
    }



    public QueryBuilder sum(String column,String alias){
        measures.add(new Measure(column,"SUM",alias));
        return this;
    }



    public QueryBuilder count(String column,String alias){
        measures.add(new Measure(column,"COUNT",alias));
        return this;
    }



    public QueryBuilder avg(String column,String alias){
        measures.add(new Measure(column,"AVG",alias));
        return this;
    }



    public QueryBuilder where(ConditionNode condition){

        this.where = condition;

        return this;
    }



    public QueryBuilder having(ConditionNode condition){

        this.having = condition;

        return this;
    }



    public QueryBuilder join(
            Join.Type type,
            String table,
            String left,
            String right
    ){

        if(query.getJoins()==null){

            query.setJoins(new ArrayList<>());

        }


        query.getJoins()
                .add(new Join(type,table,left,right));


        return this;
    }




    public Query build(){
        query.setFields(fields);
        query.setDimensions(dimensions);
        query.setMeasures(measures);
        query.setWhere(where);
        query.setHaving(having);
        return query;
    }



    // ================= condition =================


    public static LeafCondition eq(String column,Object value){
        return new LeafCondition(column,"=",value);
    }

    /**
     * 不等于
     */
    public static LeafCondition ne(String column, Object value) {
        return new LeafCondition(column, "<>", value);
    }



    public static LeafCondition gt(String column,Object value){
        return new LeafCondition(column,">",value);
    }



    public static LeafCondition gte(String column,Object value){
        return new LeafCondition(column,">=",value);
    }



    public static LeafCondition lt(String column,Object value){
        return new LeafCondition(column,"<",value);
    }



    public static LeafCondition lte(String column,Object value){
        return new LeafCondition(column,"<=",value);
    }



    public static LeafCondition like(String column,Object value){
        return new LeafCondition(column,"LIKE",value);
    }

    public static LeafCondition isNull(String column) {
        return new LeafCondition(column, "IS NULL", "");
    }



    public static LeafCondition in(String column,Collection<?> values){
        return new LeafCondition(column,"IN",values);
    }



    public static CompositeCondition and(ConditionNode... nodes){
        CompositeCondition cc=new CompositeCondition();
        cc.setType(CompositeCondition.Type.AND);
        cc.setConditions(Arrays.asList(nodes));
        return cc;
    }



    public static CompositeCondition or(ConditionNode... nodes){
        CompositeCondition cc=new CompositeCondition();
        cc.setType(CompositeCondition.Type.OR);
        cc.setConditions(Arrays.asList(nodes));
        return cc;
    }

}
