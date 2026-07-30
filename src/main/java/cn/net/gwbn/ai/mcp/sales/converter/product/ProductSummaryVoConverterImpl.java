package cn.net.gwbn.ai.mcp.sales.converter.product;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.product.ProductItemVo;
import cn.net.gwbn.ai.mcp.sales.api.product.ProductSummaryVo;
import cn.net.gwbn.ai.mcp.sales.converter.BaseSummaryVoConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author lixiaofeng
 * @date 6/29/26 AM9:12
 **/
public class ProductSummaryVoConverterImpl extends BaseSummaryVoConverter<ProductSummaryVo> implements ProductSummaryVoConverter {

    /**
     * 产品维度列
     */
    private static final String[] DIMENSION_COLUMNS = {
            "product_spec",
            "period_num",
            "pricing_range",
            "specification",
            "product_specification",
            "spec_name"
    };

    @Override
    public ProductSummaryVo convert(QueryResult source) {
        return null;
    }

    @Override
    public ProductSummaryVo convert(int year, int month, int day, String dimension, String type, QueryResult dayResult, QueryResult monthResult) {

        ProductSummaryVo vo = new ProductSummaryVo();
        vo.setYear(year);
        vo.setMonth(month);
        vo.setDay(day);
        vo.setDimension(dimension);

        boolean incomeMode = !"COUNT".equalsIgnoreCase(type);

        vo.setUnit(incomeMode ? "元" : "户");

        switch (dimension) {

            case "带宽":
                vo.setDimensionDescription("dimension 表示带宽(M)，例如：100M、200M、500M");
                break;

            case "时长":
                vo.setDimensionDescription("dimension 表示时长(月)，例如：12个月、24个月、36个月");
                break;

            case "价格区间":
                vo.setDimensionDescription("dimension 表示价格区间(元)，例如：100元-500元");
                break;

            default:
                break;
        }

        Map<String, ProductItemVo> productMap = new LinkedHashMap<>();

        merge(dayResult, productMap, incomeMode, true);

        merge(monthResult, productMap, incomeMode, false);

        vo.setProductItems(new ArrayList<>(productMap.values()));

        return vo;
    }

    /**
     * 合并查询结果
     */
    private void merge(QueryResult result, Map<String, ProductItemVo> productMap, boolean incomeMode, boolean day) {

        if (result == null || result.getRows().isEmpty()) {
            return;
        }

        Map<String, Integer> columnMap = getColumnIndexMap(result.getColumns());

        int dimensionIndex = resolveDimensionColumn(columnMap);

        for (List<Object> row : result.getRows()) {

            String dimension = getDimensionValue(row, dimensionIndex);

            ProductItemVo item = productMap.computeIfAbsent(dimension, key -> {
                ProductItemVo vo = new ProductItemVo();
                vo.setDimension(key);
                return vo;
            });

            long value = getLong(row, columnMap, "metric_value", "total_sale_price", "sale_price");

            if (incomeMode) {
                BigDecimal yuan = fenToYuan(value);
                if (day) {
                    item.setDayIncome(yuan);
                    item.setDisplayDayIncome(yuanToWan(yuan));
                } else {
                    item.setMonthIncome(yuan);
                    item.setDisplayMonthIncome(yuanToWan(yuan));
                }
            } else {
                BigDecimal count = BigDecimal.valueOf(value);
                if (day) {
                    item.setDayIncome(count);
                } else {
                    item.setMonthIncome(count);
                }
            }
        }
    }

    /**
     * 获取维度列
     */
    private int resolveDimensionColumn(Map<String, Integer> columnMap) {

        for (String column : DIMENSION_COLUMNS) {

            Integer index = columnMap.get(column);

            if (index != null) {
                return index;
            }
        }

        throw new IllegalStateException("Unknown product dimension column, supported=" + Arrays.toString(DIMENSION_COLUMNS) + ", actual=" + columnMap.keySet());
    }

    /**
     * 获取维度值
     */
    private String getDimensionValue(List<Object> row, int dimensionIndex) {

        if (dimensionIndex >= row.size()) {
            return "";
        }

        Object value = row.get(dimensionIndex);

        return value == null ? "" : value.toString();
    }

    /**
     * 分 -> 元
     */
    private BigDecimal fenToYuan(long fen) {

        return BigDecimal.valueOf(fen).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    /**
     * 元 -> 万元
     */
    private String yuanToWan(BigDecimal yuan) {

        if (yuan == null) {
            return "0.00万元";
        }

        return yuan.divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP).toPlainString() + "万元";
    }
}