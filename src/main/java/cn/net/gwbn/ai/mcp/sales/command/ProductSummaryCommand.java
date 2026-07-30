package cn.net.gwbn.ai.mcp.sales.command;

import cn.net.gwbn.ai.mcp.engine.QueryBuilder;
import cn.net.gwbn.ai.mcp.engine.QueryEngine;
import cn.net.gwbn.ai.mcp.engine.ast.ConditionNode;
import cn.net.gwbn.ai.mcp.engine.model.Query;
import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.product.ProductSummaryVo;
import cn.net.gwbn.ai.mcp.sales.converter.product.ProductSummaryVoConverter;
import cn.net.gwbn.ai.mcp.sales.model.CityEntity;
import cn.net.gwbn.ai.mcp.sales.repository.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author lixiaofeng
 * @date 6/29/26 AM8:58
 **/
public class ProductSummaryCommand {

    private static final Logger log =
            LoggerFactory.getLogger(ProductSummaryCommand.class);

    private static final String METRIC = "metric_value";

    private static final String COUNT = "COUNT";

    private final QueryEngine queryEngine;

    private final ProductSummaryVoConverter converter;

    private final CityRepository cityRepository;

    public ProductSummaryCommand(QueryEngine queryEngine, ProductSummaryVoConverter converter, CityRepository cityRepository) {

        this.queryEngine = queryEngine;
        this.converter = converter;
        this.cityRepository = cityRepository;
    }

    /**
     * 单城市产品统计
     */
    public ProductSummaryVo getProductSummaryVo(int year, int month, int day, String cityName, String dimension, String type) {

        return querySummary(year, month, day, Collections.singletonList(cityName), dimension, type);
    }

    /**
     * 多城市产品统计
     */
    public ProductSummaryVo getAllCityProductSummaryVo(int year, int month, int day, String cityType, String dimension, String type) {

        return querySummary(year, month, day, loadCityNames(cityType), dimension, type);
    }

    /**
     * 查询统计
     */
    private ProductSummaryVo querySummary(int year, int month, int day, List<String> cityNames, String dimension, String type) {

        log.info("Product summary query, year={}, month={}, day={}, dimension={}, cities={}, type={}", year, month, day, dimension, cityNames, type);

        QueryResult dayResult = queryEngine.query(buildQuery(year, month, day, cityNames, dimension, type, true));

        QueryResult monthResult = queryEngine.query(buildQuery(year, month, day, cityNames, dimension, type, false));

        logResult("day", dayResult);
        logResult("month", monthResult);

        return converter.convert(year, month, day, dimension, type, dayResult, monthResult);
    }

    /**
     * 构建查询
     */
    private Query buildQuery(int year, int month, int day, List<String> cityNames, String dimension, String type, boolean dayQuery) {

        QueryBuilder builder = QueryBuilder.table("sale_order_statistic s");

        builder.dimension(resolveDimensionColumn(dimension));

        List<ConditionNode> conditions = new ArrayList<>();

        conditions.add(QueryBuilder.eq("s.year", year));
        conditions.add(QueryBuilder.eq("s.month", month));
        conditions.add(QueryBuilder.eq("s.product_name", "宽带产品"));

        if (dayQuery) {
            conditions.add(QueryBuilder.eq("s.day", day));
        }

        if (cityNames != null && !cityNames.isEmpty()) {

            if (cityNames.size() == 1) {
                conditions.add(QueryBuilder.eq("s.city_name", cityNames.get(0)));
            } else {
                conditions.add(QueryBuilder.in("s.city_name", cityNames));}
        }

        builder.where(QueryBuilder.and(conditions.toArray(new ConditionNode[0])));

        aggregate(builder, type);

        return builder.build();
    }

    /**
     * 产品统计维度
     */
    private String resolveDimensionColumn(String dimension) {

        return switch (dimension) {

            case "带宽" -> "s.product_spec";

            case "时长" -> "s.period_num";

            case "价格区间" -> "s.pricing_range";

            default -> throw new IllegalArgumentException("Unsupported dimension: " + dimension);
        };
    }

    /**
     * 聚合方式
     */
    private void aggregate(QueryBuilder builder, String type) {
        if (COUNT.equalsIgnoreCase(type)) {
            builder.count("s.customer_order_id", METRIC);

        } else {
            builder.sum("s.sale_price", METRIC);
        }
    }

    /**
     * 查询城市
     */
    private List<String> loadCityNames(String cityType) {

        return cityRepository.findCityEntitiesByEnableAndType(true, cityType)
                .stream()
                .map(CityEntity::getName)
                .toList();
    }

    /**
     * 日志
     */
    private void logResult(String name, QueryResult result) {

        if (result == null) {
            return;
        }

        log.debug("{} columns: {}", name, result.getColumns());
        log.debug("{} rows: {}", name, result.getRows());
    }
}