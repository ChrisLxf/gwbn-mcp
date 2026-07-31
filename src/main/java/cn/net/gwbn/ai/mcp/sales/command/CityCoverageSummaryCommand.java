package cn.net.gwbn.ai.mcp.sales.command;

import cn.net.gwbn.ai.mcp.engine.QueryBuilder;
import cn.net.gwbn.ai.mcp.engine.QueryEngine;
import cn.net.gwbn.ai.mcp.engine.ast.ConditionNode;
import cn.net.gwbn.ai.mcp.engine.model.Query;
import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.coverage.CityCoverageSummaryVo;
import cn.net.gwbn.ai.mcp.sales.converter.coverage.CityCoverageSummaryVoConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiaofeng
 * @date 7/30/26 PM3:17
 **/
public class CityCoverageSummaryCommand {


    private final QueryEngine queryEngine;

    private final CityCoverageSummaryVoConverter  cityCoverageSummaryVoConverter;

    public CityCoverageSummaryCommand(QueryEngine queryEngine, CityCoverageSummaryVoConverter cityCoverageSummaryVoConverter) {
        this.queryEngine = queryEngine;
        this.cityCoverageSummaryVoConverter = cityCoverageSummaryVoConverter;
    }

    public CityCoverageSummaryVo execute(List<String> cityNames) {
        QueryResult result = queryEngine.query(builderQuery(cityNames));
        return cityCoverageSummaryVoConverter.convert(result);
    }


    private Query builderQuery(List<String> cityNames) {

        QueryBuilder builder = QueryBuilder.table("city_coverage s");

        builder.select("city_name");
        builder.select("city_code");
        builder.select("coverage_users");

        List<ConditionNode> conditions = new ArrayList<>();

        if (cityNames != null && !cityNames.isEmpty()) {
            if (cityNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.city_name", cityNames.get(0)));
            } else {
                conditions.add(QueryBuilder.in("s.city_name", cityNames));
            }
        }

        // 有查询条件才拼接where
        if (!conditions.isEmpty()) {
            builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));
        }

        return builder.build();
    }
}
