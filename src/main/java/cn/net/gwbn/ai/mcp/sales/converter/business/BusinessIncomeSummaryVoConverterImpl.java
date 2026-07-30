package cn.net.gwbn.ai.mcp.sales.converter.business;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.business.BusinessSummaryVo;
import cn.net.gwbn.ai.mcp.sales.api.business.BusinessVo;
import cn.net.gwbn.ai.mcp.sales.converter.BaseSummaryVoConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lixiaofeng
 * @date 6/28/26 PM5:02
 **/
public class BusinessIncomeSummaryVoConverterImpl extends BaseSummaryVoConverter<BusinessSummaryVo> implements BusinessIncomeSummaryVoConverter {

    @Override
    public BusinessSummaryVo convert(QueryResult source) {
        return null;
    }

    @Override
    public BusinessSummaryVo convert(int year, int month, int day, String type, QueryResult dayResult, QueryResult monthResult) {

        BusinessSummaryVo summary = new BusinessSummaryVo();
        summary.setYear(year);
        summary.setMonth(month);
        summary.setDay(day);

        boolean incomeMode = !"COUNT".equalsIgnoreCase(type);

        summary.setUnit(incomeMode ? "元" : "户");

        Map<String, BusinessVo> businessMap = new LinkedHashMap<>();

        // -----------------------------
        // 日统计
        // -----------------------------
        if (dayResult != null && !dayResult.getRows().isEmpty()) {

            Map<String, Integer> columnMap = getColumnIndexMap(dayResult.getColumns());

            for (List<Object> row : dayResult.getRows()) {

                String businessName = getString(row, columnMap, "business_name");
                long value = getLong(row, columnMap, "metric_value");

                BusinessVo businessVo = businessMap.computeIfAbsent(businessName, k -> {
                    BusinessVo item = new BusinessVo();
                    item.setBusinessName(k);
                    return item;
                });

                if (incomeMode) {
                    BigDecimal yuan = fenToYuan(value);
                    businessVo.setDayIncome(yuan);
                    businessVo.setDisplayDayIncome(formatMoney(yuan));
                } else {
                    BigDecimal count = BigDecimal.valueOf(value);
                    businessVo.setDayIncome(count);
                    businessVo.setDisplayDayIncome(count.stripTrailingZeros().toPlainString());
                }
            }
        }

        // -----------------------------
        // 月统计
        // -----------------------------
        if (monthResult != null && !monthResult.getRows().isEmpty()) {

            Map<String, Integer> columnMap = getColumnIndexMap(monthResult.getColumns());
            for (List<Object> row : monthResult.getRows()) {
                String businessName = getString(row, columnMap, "business_name");
                long value = getLong(row, columnMap, "metric_value");
                BusinessVo businessVo = businessMap.computeIfAbsent(businessName, k -> {
                    BusinessVo item = new BusinessVo();
                    item.setBusinessName(k);
                    return item;
                });

                if (incomeMode) {
                    BigDecimal yuan = fenToYuan(value);
                    businessVo.setMonthIncome(yuan);
                    businessVo.setDisplayMonthIncome(formatMoney(yuan));
                } else {
                    BigDecimal count = BigDecimal.valueOf(value);
                    businessVo.setMonthIncome(count);
                    businessVo.setDisplayMonthIncome(count.stripTrailingZeros().toPlainString());
                }
            }
        }

        summary.setBusinessList(new ArrayList<>(businessMap.values()));

        return summary;
    }


    private BigDecimal fenToYuan(long fen) {
        return BigDecimal.valueOf(fen).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    protected String formatMoney(BigDecimal yuan) {

        if (yuan == null) {
            return "0元";
        }

        if (yuan.compareTo(BigDecimal.valueOf(100_000_000)) >= 0) {
            return yuan.divide(BigDecimal.valueOf(100_000_000), 2, RoundingMode.HALF_UP)
                    .toPlainString() + "亿元";
        }

        if (yuan.compareTo(BigDecimal.valueOf(10_000)) >= 0) {
            return yuan.divide(BigDecimal.valueOf(10_000), 2, RoundingMode.HALF_UP)
                    .toPlainString() + "万元";
        }

        return yuan.setScale(2, RoundingMode.HALF_UP).toPlainString() + "元";
    }
}