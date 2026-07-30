package cn.net.gwbn.ai.mcp.sales.converter.user;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.user.ExistUserSummaryVo;
import cn.net.gwbn.ai.mcp.sales.api.user.UserItemVo;
import cn.net.gwbn.ai.mcp.sales.converter.BaseSummaryVoConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 在网用户汇总转换器
 *
 * @author lixiaofeng
 * @date 7/28/26 PM1:48
 **/
public class ExistUserSummaryVoConverterImpl extends BaseSummaryVoConverter<ExistUserSummaryVo> implements ExistUserSummaryVoConverter {


    @Override
    public ExistUserSummaryVo convert(int year, int month, QueryResult source) {

        ExistUserSummaryVo summary = new ExistUserSummaryVo();
        summary.setYear(year);
        summary.setMonth(month);
        summary.setUnit("户");

        List<UserItemVo> userItems = new ArrayList<>();

        long totalUserCount = 0L;


        if (source != null && !source.getRows().isEmpty()) {

            Map<String, Integer> colIndex = getColumnIndexMap(source.getColumns());

            for (List<Object> row : source.getRows()) {
                String cityId = getString(row, colIndex, "region_id");
                String cityName = getString(row, colIndex, "region_name");
                long userCount = getLong(row, colIndex, "metric_value");
                UserItemVo item = new UserItemVo();
                item.setCityId(cityId);
                item.setCityName(cityName);
                item.setUserCount(userCount);
                item.setUnit("户");
                userItems.add(item);

                totalUserCount += userCount;
            }
        }


        summary.setTotalUserCount(totalUserCount);
        summary.setUserItems(userItems);


        return summary;
    }

    @Override
    public ExistUserSummaryVo convert(int year, int month,int day, QueryResult currentExistUser, QueryResult renewalT0User, QueryResult newUser) {

        ExistUserSummaryVo summary = new ExistUserSummaryVo();

        summary.setYear(year);
        summary.setMonth(month);
        summary.setDay(day);
        summary.setUnit("户");


        // key: cityId
        Map<String, UserItemVo> cityMap = new java.util.LinkedHashMap<>();


        // 处理月初快照有效用户
        mergeUserResult(cityMap, currentExistUser, "region_id", "region_name");


        // 处理到期续费用户
        mergeUserResult(cityMap, renewalT0User, "city_id", "city_name");


        // 处理新装用户
        mergeUserResult(cityMap, newUser, "city_id", "city_name");


        long totalUserCount = 0L;

        List<UserItemVo> userItems = new ArrayList<>();

        for (UserItemVo item : cityMap.values()) {
            totalUserCount += item.getUserCount();
            userItems.add(item);
        }


        summary.setTotalUserCount(totalUserCount);
        summary.setUserItems(userItems);

        return summary;
    }

    @Override
    public ExistUserSummaryVo convert(QueryResult source) {
        return null;
    }

    private void mergeUserResult(Map<String, UserItemVo> cityMap, QueryResult source, String cityIdColumn, String cityNameColumn) {

        if (source == null || source.getRows().isEmpty()) {
            return;
        }


        Map<String, Integer> colIndex = getColumnIndexMap(source.getColumns());


        for (List<Object> row : source.getRows()) {
            String cityId = getString(row, colIndex, cityIdColumn);
            String cityName = getString(row, colIndex, cityNameColumn);

            long userCount = getLong(row, colIndex, "metric_value");

            UserItemVo item = cityMap.computeIfAbsent(cityId, k -> {
                UserItemVo vo = new UserItemVo();
                vo.setCityId(cityId);
                vo.setCityName(cityName);
                vo.setUnit("户");
                vo.setUserCount(0L);

                return vo;
            });

            item.setUserCount(item.getUserCount() + userCount);
        }
    }

}