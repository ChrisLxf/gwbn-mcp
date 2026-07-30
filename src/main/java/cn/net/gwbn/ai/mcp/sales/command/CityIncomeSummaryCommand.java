package cn.net.gwbn.ai.mcp.sales.command;

import cn.net.gwbn.ai.mcp.sales.api.income.CityIncomeSummaryVo;
import cn.net.gwbn.ai.mcp.sales.converter.income.CityIncomeSummaryVoConverter;
import cn.net.gwbn.ai.mcp.engine.QueryBuilder;
import cn.net.gwbn.ai.mcp.engine.QueryEngine;
import cn.net.gwbn.ai.mcp.engine.ast.ConditionNode;
import cn.net.gwbn.ai.mcp.engine.model.Query;
import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.model.CityEntity;
import cn.net.gwbn.ai.mcp.sales.repository.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市收入查询命令
 *
 * @author lixiaofeng
 * @date 6/28/26 AM10:46
 **/
public class CityIncomeSummaryCommand {

    private static final Logger log = LoggerFactory.getLogger(CityIncomeSummaryCommand.class);

    private final QueryEngine queryEngine;

    private final CityIncomeSummaryVoConverter cityIncomeSummaryVoConverter;

    private final CityRepository cityRepository;

    public CityIncomeSummaryCommand(QueryEngine queryEngine, CityIncomeSummaryVoConverter cityIncomeSummaryVoConverter, CityRepository cityRepository) {

        this.queryEngine = queryEngine;
        this.cityIncomeSummaryVoConverter = cityIncomeSummaryVoConverter;
        this.cityRepository = cityRepository;
    }

    /**
     * 查询单城市收入
     */
    public CityIncomeSummaryVo getCityIncomeVo(int year, int month, int day, String cityName) {
        return queryIncomeSummary(year, month, day, buildCityCondition(cityName));
    }

    /**
     * 查询多个城市收入
     */
    public CityIncomeSummaryVo getAllCityIncomeVo(int year, int month, int day, String type) {

        List<String> cityNames = cityRepository
                .findCityEntitiesByEnableAndType(true,type)
                .stream()
                .map(CityEntity::getName)
                .toList();

        return queryIncomeSummary(year, month, day, buildCityCondition(cityNames));
    }

    /**
     * 查询收入（日收入 + 月收入）
     */
    private CityIncomeSummaryVo queryIncomeSummary(int year, int month, int day, ConditionNode cityCondition) {

        // 查询日收入
        QueryResult dayResult = queryIncome(year, month, day, cityCondition);

        // 查询月收入
        QueryResult monthResult = queryIncome(year, month, null, cityCondition);

        // 转换为展示格式
        return cityIncomeSummaryVoConverter.convert(year, month, day, dayResult, monthResult);
    }

    /**
     * 查询一次收入
     */
    private QueryResult queryIncome(int year, int month, Integer day, ConditionNode cityCondition) {

        Query query = buildCityIncomeQuery(year, month, day, cityCondition);

        QueryResult result = queryEngine.query(query);

        log.debug("columns={}", result.getColumns());
        log.debug("rows={}", result.getRows());

        return result;
    }

    /**
     * 构造城市查询条件（单城市）
     */
    private ConditionNode buildCityCondition(String cityName) {

        if (cityName == null || cityName.isBlank()) {
            return null;
        }

        return QueryBuilder.eq("s.city_name", cityName);
    }

    /**
     * 构造城市查询条件（多个城市）
     */
    private ConditionNode buildCityCondition(List<String> cityNames) {

        if (cityNames == null || cityNames.isEmpty()) {
            return null;
        }

        return QueryBuilder.in("s.city_name", cityNames);
    }

    /**
     * 构造收入查询
     */
    private Query buildCityIncomeQuery(int year, int month, Integer day, ConditionNode cityCondition) {

        QueryBuilder builder = QueryBuilder.table("sale_order_statistic s")
                .dimension("s.city_id")
                .dimension("s.city_name")
                .sum("s.sale_price", "total_sale_price");

        List<ConditionNode> conditions = new ArrayList<>();

        conditions.add(QueryBuilder.eq("s.year", year));
        conditions.add(QueryBuilder.eq("s.month", month));

        if (day != null) {
            conditions.add(QueryBuilder.eq("s.day", day));
        }

        if (cityCondition != null) {
            conditions.add(cityCondition);
        }

        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));

        return builder.build();
    }
}