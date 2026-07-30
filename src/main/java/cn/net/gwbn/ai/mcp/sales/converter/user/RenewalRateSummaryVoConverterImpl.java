package cn.net.gwbn.ai.mcp.sales.converter.user;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.user.RenewalRateItem;
import cn.net.gwbn.ai.mcp.sales.api.user.RenewalRateSummaryVo;
import cn.net.gwbn.ai.mcp.sales.converter.BaseSummaryVoConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lixiaofeng
 * @date 7/28/26 PM3:31
 **/
public class RenewalRateSummaryVoConverterImpl extends BaseSummaryVoConverter<RenewalRateSummaryVo> implements RenewalRateSummaryVoConverter {


    @Override
    public RenewalRateSummaryVo convert(QueryResult source) {
        return null;
    }


    /**
     * 转换续费率统计结果
     *
     * @param year              年
     * @param month             月
     * @param t                 T+n周期
     * @param allRenewalResult  全部到期用户结果
     * @param renewalUserResult 已续费用户结果
     * @return 续费率统计结果
     */
    @Override
    public RenewalRateSummaryVo convert(int year, int month, int t, QueryResult allRenewalResult, QueryResult renewalUserResult) {

        RenewalRateSummaryVo summary = new RenewalRateSummaryVo();

        summary.setYear(year);
        summary.setMonth(month);
        summary.setT(t);

        Map<String, RenewalRateItem> cityMap = new LinkedHashMap<>();


        // 合并应续费用户
        mergeRenewalResult(allRenewalResult, cityMap, true);


        // 合并已续费用户
        mergeRenewalResult(renewalUserResult, cityMap, false);


        // 计算续费率
        for (RenewalRateItem item : cityMap.values()) {
            calculateRenewalRate(item);
        }
        summary.setRenewalRateList(new ArrayList<>(cityMap.values()));


        return summary;
    }


    /**
     * 合并续费数据
     *
     * @param result  查询结果
     * @param cityMap 城市数据
     * @param total   true: 应续费用户 false: 已续费用户
     */
    private void mergeRenewalResult(QueryResult result, Map<String, RenewalRateItem> cityMap, boolean total) {


        if (result == null || result.getRows() == null || result.getRows().isEmpty()) {
            return;
        }

        Map<String, Integer> colIndex = getColumnIndexMap(result.getColumns());


        for (List<Object> row : result.getRows()) {

            String cityId;
            String cityName;

            if (total) {
                cityId = getString(row, colIndex, "region_id");
                cityName = getString(row, colIndex, "region_name");
            } else {
                cityId = getString(row, colIndex, "city_id");
                cityName = getString(row, colIndex, "city_name");
            }

            long count = getLong(row, colIndex, "metric_value");
            RenewalRateItem item = cityMap.computeIfAbsent(cityId, key -> {
                RenewalRateItem vo = new RenewalRateItem();
                vo.setCityId(cityId);
                vo.setCityName(cityName);
                return vo;
            });


            if (total) {
                // 应续费用户数
                item.setTotalRenewalCount(count);
            } else {
                // 已续费用户数
                item.setCurrentRenewalCount(count);

            }
        }
    }


    /**
     * 计算续费率
     * <p>
     * 续费率 = 已续费用户数 / 应续费用户数
     */
    private void calculateRenewalRate(RenewalRateItem item) {


        long total = item.getTotalRenewalCount();
        long current = item.getCurrentRenewalCount();


        if (total <= 0) {
            item.setRenewalRate("0%");
            return;
        }

        BigDecimal rate = BigDecimal.valueOf(current).divide(BigDecimal.valueOf(total), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        item.setRenewalRate(rate.setScale(2, RoundingMode.HALF_UP).toPlainString() + "%");
    }

}
