package cn.net.gwbn.ai.mcp.sales.command;

import cn.net.gwbn.ai.mcp.engine.QueryBuilder;
import cn.net.gwbn.ai.mcp.engine.QueryEngine;
import cn.net.gwbn.ai.mcp.engine.ast.ConditionNode;
import cn.net.gwbn.ai.mcp.engine.model.Query;
import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.business.BusinessSummaryVo;
import cn.net.gwbn.ai.mcp.sales.converter.business.BusinessIncomeSummaryVoConverter;
import cn.net.gwbn.ai.mcp.sales.model.CityEntity;
import cn.net.gwbn.ai.mcp.sales.repository.CityRepository;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lixiaofeng
 * @date 6/28/26 PM5:03
 **/
public class BusinessSummaryCommand {

    private static final Logger log = LoggerFactory.getLogger(BusinessSummaryCommand.class);

    private static final String METRIC = "metric_value";

    private static final String COUNT = "COUNT";

    private final QueryEngine queryEngine;

    private final BusinessIncomeSummaryVoConverter converter;

    private final CityRepository cityRepository;

    public BusinessSummaryCommand(QueryEngine queryEngine, BusinessIncomeSummaryVoConverter converter, CityRepository cityRepository) {
        this.queryEngine = queryEngine;
        this.converter = converter;
        this.cityRepository = cityRepository;
    }

    /**
     * 单城市业务统计
     */
    public BusinessSummaryVo getBusinessIncomeSummaryVo(int year, int month, int day, String cityName, String type) {

        return querySummary(year, month, day, Collections.singletonList(cityName), type);
    }

    /**
     * 多城市业务统计
     */
    public BusinessSummaryVo getAllBusinessIncomeSummaryVo(int year, int month, int day, String cityType, String type) {
        return querySummary(year, month, day, loadCityNames(cityType), type);
    }

    /**
     * 查询业务统计
     */
    private BusinessSummaryVo querySummary(int year, int month, int day, List<String> cityNames, String type) {

        log.info("Business summary query, year={}, month={}, day={}, cities={}, type={}", year, month, day, cityNames, type);

        QueryResult dayResult = queryEngine.query(buildQuery(year, month, day, cityNames, type, true));

        QueryResult monthResult = queryEngine.query(buildQuery(year, month, day, cityNames, type, false));

        logResult("day", dayResult);
        logResult("month", monthResult);
        return converter.convert(year, month, day, type, dayResult, monthResult);
    }

    /**
     * 构建查询
     */
    private Query buildQuery(int year, int month, int day, List<String> cityNames, String type, boolean dayQuery) {

        QueryBuilder builder = QueryBuilder.table("sale_order_statistic s").dimension("s.business_name");

        List<ConditionNode> conditions = new ArrayList<>();

        conditions.add(QueryBuilder.eq("s.year", year));
        conditions.add(QueryBuilder.eq("s.month", month));

        if (dayQuery) {
            conditions.add(QueryBuilder.eq("s.day", day));
        }

        if (cityNames != null && !cityNames.isEmpty()) {

            if (cityNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.city_name", cityNames.get(0)));
            } else {
                conditions.add(QueryBuilder.in("s.city_name", cityNames));
            }
        }

        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));

        aggregate(builder, type);

        return builder.build();
    }

    /**
     * 聚合方式
     */private void aggregate(QueryBuilder builder, String type) {

        if (COUNT.equalsIgnoreCase(type)) {
            builder.count("s.customer_order_id", METRIC);
        } else {
            builder.sum("s.sale_price", METRIC);
        }
    }

    /**
     * 查询城市名称
     */
    private List<String> loadCityNames(String cityType) {

        List<CityEntity> cities = cityRepository.findCityEntitiesByEnableAndType(true, cityType);

        return cities.stream().map(CityEntity::getName).toList();
    }

    /**
     * 打印日志
     */
    private void logResult(String name, QueryResult result) {

        if (result == null) {
            log.debug("{} result is null", name);
            return;
        }

        log.debug("{} columns: {}", name, result.getColumns());
        log.debug("{} rows: {}", name, result.getRows());
    }
}