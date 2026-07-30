package cn.net.gwbn.ai.mcp.sales.converter.income;

import cn.net.gwbn.ai.mcp.sales.api.income.CityIncomeSummaryVo;
import cn.net.gwbn.ai.mcp.sales.api.income.CityIncomeVo;
import cn.net.gwbn.ai.mcp.sales.converter.BaseSummaryVoConverter;
import cn.net.gwbn.ai.mcp.engine.result.QueryResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lixiaofeng
 * @date 6/28/26 AM10:49
 **/
public class CityIncomeSummaryVoConverterImpl extends BaseSummaryVoConverter<CityIncomeSummaryVo> implements CityIncomeSummaryVoConverter {

    @Override
    public CityIncomeSummaryVo convert(QueryResult source) {
        return null;
    }

    @Override
    public CityIncomeSummaryVo convert(int year, int month, int day, QueryResult cityDayIncomeSummary, QueryResult cityMonthIncomeSummary) {

        CityIncomeSummaryVo summary = new CityIncomeSummaryVo();
        summary.setYear(year);
        summary.setMonth(month);
        summary.setDay(day);
        summary.setUnit("元");

        Map<String, CityIncomeVo> cityMap = new HashMap<>();

        long totalDayIncomeFen = mergeIncome(cityDayIncomeSummary, cityMap, true);
        long totalMonthIncomeFen = mergeIncome(cityMonthIncomeSummary, cityMap, false);

        summary.setIncomeVos(new ArrayList<>(cityMap.values()));

        // 原始值（元）
        BigDecimal totalDayIncome = fenToYuan(totalDayIncomeFen);
        BigDecimal totalMonthIncome = fenToYuan(totalMonthIncomeFen);

        summary.setTotalDayIncome(totalDayIncome);
        summary.setTotalMonthIncome(totalMonthIncome);

        // 展示值（万元）
        summary.setDisplayTotalDayIncome(formatWan(totalDayIncome));
        summary.setDisplayTotalMonthIncome(formatWan(totalMonthIncome));

        return summary;
    }

    /**
     * 合并收入数据
     *
     * @return 汇总收入（单位：分）
     */
    private long mergeIncome(QueryResult result, Map<String, CityIncomeVo> cityMap, boolean isDay) {

        if (result == null || result.getRows().isEmpty()) {
            return 0L;
        }

        Map<String, Integer> colIndex = getColumnIndexMap(result.getColumns());

        long total = 0L;

        for (List<Object> row : result.getRows()) {

            String cityId = getString(row, colIndex, "city_id");
            String cityName = getString(row, colIndex, "city_name");
            long incomeFen = getLong(row, colIndex, "total_sale_price");

            BigDecimal incomeYuan = fenToYuan(incomeFen);

            CityIncomeVo vo = cityMap.computeIfAbsent(cityId, k -> {
                CityIncomeVo item = new CityIncomeVo();
                item.setCityId(cityId);
                item.setCityName(cityName);
                item.setUnit("元");
                return item;
            });

            if (isDay) {
                vo.setDayIncome(incomeYuan);
                vo.setDisplayDayIncome(formatWan(incomeYuan));
            } else {
                vo.setMonthIncome(incomeYuan);
                vo.setDisplayMonthIncome(formatWan(incomeYuan));
            }

            total += incomeFen;
        }

        return total;
    }

    /**
     * 分 -> 元
     */
    private BigDecimal fenToYuan(long fen) {
        return BigDecimal.valueOf(fen).movePointLeft(2).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 元 -> 万元（展示）
     */
    private String formatWan(BigDecimal yuan) {
        if (yuan == null) {
            return "0.00万元";
        }

        return yuan.divide(BigDecimal.valueOf(10_000), 2, RoundingMode.HALF_UP).toPlainString() + "万元";
    }
}