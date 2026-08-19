package cn.net.gwbn.ai.mcp.sales.converter.user;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.user.ActiveUserSummaryVo;
import cn.net.gwbn.ai.mcp.sales.api.user.UserItemVo;
import cn.net.gwbn.ai.mcp.sales.converter.BaseSummaryVoConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lixiaofeng
 * @date 8/19/26 PM2:03
 **/
public class ActiveUserSummaryVoConverterImpl extends BaseSummaryVoConverter<ActiveUserSummaryVo> implements ActiveUserSummaryVoConverter {


    @Override
    public ActiveUserSummaryVo convert(QueryResult source) {

        ActiveUserSummaryVo summary = new ActiveUserSummaryVo();
        summary.setUnit("户");
        List<UserItemVo> userItems = new ArrayList<>();

        long totalUserCount = 0L;

        if (source != null && !source.getRows().isEmpty()) {
            Map<String, Integer> colIndex = getColumnIndexMap(source.getColumns());
            for (List<Object> row : source.getRows()) {
                String cityId = getString(row, colIndex, "city_id");
                String cityName = getString(row, colIndex, "city_name");
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

}