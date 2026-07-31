package cn.net.gwbn.ai.mcp.sales.converter.coverage;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.coverage.CityCoverageItem;
import cn.net.gwbn.ai.mcp.sales.api.coverage.CityCoverageSummaryVo;
import cn.net.gwbn.ai.mcp.sales.converter.BaseSummaryVoConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lixiaofeng
 * @date 7/30/26 PM3:52
 **/
public class CityCoverageSummaryVoConverterImpl extends BaseSummaryVoConverter<CityCoverageSummaryVo> implements CityCoverageSummaryVoConverter {


    private static final String UNIT = "户";


    @Override
    public CityCoverageSummaryVo convert(QueryResult source) {

        CityCoverageSummaryVo summary = new CityCoverageSummaryVo();
        summary.setUnit(UNIT);
        List<CityCoverageItem> items = new ArrayList<>();
        long totalCoverageNumber = 0L;

        if (source != null && source.getRows() != null && !source.getRows().isEmpty()) {

            Map<String, Integer> colIndex = getColumnIndexMap(source.getColumns());

            for (List<Object> row : source.getRows()) {
                CityCoverageItem item = new CityCoverageItem();
                String cityId = getString(row, colIndex, "city_code");
                String cityName = getString(row, colIndex, "city_name");
                long coverageNumber = getLong(row, colIndex, "coverage_users");
                item.setCityId(cityId);
                item.setCityName(cityName);
                item.setCoverageNumber(coverageNumber);
                item.setUnit(UNIT);
                items.add(item);
                totalCoverageNumber += coverageNumber;
            }
        }

        summary.setCityCoverageItemList(items);
        summary.setTotalCoverageNumber(totalCoverageNumber);

        return summary;
    }
}