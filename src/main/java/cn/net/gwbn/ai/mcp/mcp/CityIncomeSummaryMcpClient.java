package cn.net.gwbn.ai.mcp.mcp;

import cn.net.gwbn.ai.mcp.sales.api.income.CityIncomeSummaryVo;
import cn.net.gwbn.ai.mcp.sales.command.CityIncomeSummaryCommand;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

/**
 * 城市收入查询mcp客户端
 *
 * @author lixiaofeng
 * @date 5/29/26 PM3:56
 **/
public class CityIncomeSummaryMcpClient {


    private final CityIncomeSummaryCommand cityIncomeSummaryCommand;

    public CityIncomeSummaryMcpClient(CityIncomeSummaryCommand cityIncomeSummaryCommand) {
        this.cityIncomeSummaryCommand = cityIncomeSummaryCommand;
    }


    /**
     * 所有城市收入查询
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 城市收入列表
     */
    @McpTool(name = "getAllCityIncomeSummary",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期所有城市收入统计。
                    
                    调用场景：
                    - 查询全国收入
                    - 查询多个城市收入
                    - 城市收入排名
                    - 城市收入对比
                    - 经营收入分析
                    
                    一次返回所有城市收入数据，无需重复调用多个城市接口。
                    
                    返回内容：
                    - 城市名称
                    - 当日收入
                    - 当月累计收入
                    """
    )
    public CityIncomeSummaryVo getAllCityIncomeSummary(@McpToolParam(description = "统计年份，例如：2026") int year,
                                                       @McpToolParam(description = "统计月份，取值范围：1~12，例如：5") int month,
                                                       @McpToolParam(description = "统计日期，取值范围：1~31，例如：29") int day,
                                                       @McpToolParam(description = "统计类别，可选值:自营、代管，每次只能传一个值，例如：自营") String type) {
        return cityIncomeSummaryCommand.getAllCityIncomeVo(year, month, day, type);
    }


    /**
     * 指定城市收入查询
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 城市收入列表
     */
    @McpTool(name = "getCityIncomeSummary",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期单个城市收入统计。
                    
                    调用场景：
                    - 查询某个城市收入
                    - 查询单城市经营情况
                    
                    每次只能查询一个城市。
                    
                    返回内容：
                    - 当日收入
                    - 当月累计收入
                    
                    """
    )
    public CityIncomeSummaryVo getCityIncomeSummary(@McpToolParam(description = "统计年份，例如：2026") int year,
                                                    @McpToolParam(description = "统计月份，取值范围：1~12，例如：5") int month,
                                                    @McpToolParam(description = "统计日期，取值范围：1~31，例如：29") int day,
                                                    @McpToolParam(description = "城市名称，每次只能传入一个城市，例如：北京市、上海市、青岛市") String cityName) {
        return cityIncomeSummaryCommand.getCityIncomeVo(year, month, day, cityName);
    }

}

