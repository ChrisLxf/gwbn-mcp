package cn.net.gwbn.ai.mcp.mcp;

import cn.net.gwbn.ai.mcp.sales.api.business.BusinessSummaryVo;
import cn.net.gwbn.ai.mcp.sales.command.BusinessSummaryCommand;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

/**
 * 业务收入分析
 *
 * @author lixiaofeng
 * @date 6/28/26 PM5:22
 **/
public class BusinessSummaryMcpClient {

    private final BusinessSummaryCommand businessSummaryCommand;

    public BusinessSummaryMcpClient(BusinessSummaryCommand businessSummaryCommand1) {
        this.businessSummaryCommand = businessSummaryCommand1;
    }

    @McpTool(name = "getAllCityBusinessIncome",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期全国所有城市各业务类型的收入统计。
                    
                    调用场景：
                    - 用户查询全国业务收入
                    - 用户比较不同业务类型收入
                    - 用户查看全集团各业务收入情况
                    
                    返回内容：
                    - 每种业务类型的当日收入
                    - 每种业务类型的当月累计收入
                    
                    """
    )
    public BusinessSummaryVo getAllCityBusinessIncome(@McpToolParam(description = "统计年份，例如：2026") int year,
                                                      @McpToolParam(description = "统计月份，取值范围：1~12，例如：5") int month,
                                                      @McpToolParam(description = "统计日期，取值范围：1~31，例如：29") int day,
                                                      @McpToolParam(description = "统计类别，可选值:自营、代管，每次只能传一个值，例如：自营") String type) {
        return businessSummaryCommand.getAllBusinessIncomeSummaryVo(year, month, day, type, "INCOME");
    }


    @McpTool(name = "getAllCityBusinessCount",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期全国所有城市各业务类型的数量统计。
                    
                    调用场景：
                    - 用户查询全国业务办理数量
                    - 用户比较不同业务类型办理量
                    - 用户查看全集团业务发展情况
                    
                    返回内容：
                    - 每种业务类型的当日数量
                    - 每种业务类型的当月累计数量
                    """
    )
    public BusinessSummaryVo getAllCityBusinessCount(@McpToolParam(description = "统计年份，例如：2026") int year,
                                                     @McpToolParam(description = "统计月份，取值范围：1~12，例如：5") int month,
                                                     @McpToolParam(description = "统计日期，取值范围：1~31，例如：29") int day,
                                                     @McpToolParam(description = "统计类别，可选值:自营、代管，每次只能传一个值，例如：自营") String type) {
        return businessSummaryCommand.getAllBusinessIncomeSummaryVo(year, month, day, type, "COUNT");
    }


    @McpTool(name = "getCityBusinessIncome",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期某个城市各业务类型的收入统计。
                    
                    调用场景：
                    - 用户查询某个城市业务收入
                    - 用户比较某城市不同业务类型收入
                    - 用户查看指定城市经营收入情况
                    
                    返回内容：
                    - 每种业务类型的当日收入
                    - 每种业务类型的当月累计收入

                    """
    )
    public BusinessSummaryVo getCityBusinessIncome(@McpToolParam(description = "统计年份，例如：2026") int year,
                                                   @McpToolParam(description = "统计月份，取值范围：1~12，例如：5") int month,
                                                   @McpToolParam(description = "统计日期，取值范围：1~31，例如：29") int day,
                                                   @McpToolParam(description = "城市名称，每次只能传入一个城市，例如：北京市、上海市、青岛市") String cityName) {
        return businessSummaryCommand.getBusinessIncomeSummaryVo(year, month, day, cityName, "INCOME");
    }


    @McpTool(name = "getCityBusinessCount",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期某个城市各业务类型的数量统计。
                    
                    调用场景：
                    - 用户查询某个城市业务办理数量
                    - 用户比较某城市不同业务类型办理量
                    - 用户查看指定城市业务发展情况
                    
                    返回内容：
                    - 每种业务类型的当日数量
                    - 每种业务类型的当月累计数量
                    """
    )
    public BusinessSummaryVo getCityBusinessCount(@McpToolParam(description = "统计年份，例如：2026") int year,
                                                  @McpToolParam(description = "统计月份，取值范围：1~12，例如：5") int month,
                                                  @McpToolParam(description = "统计日期，取值范围：1~31，例如：29") int day,
                                                  @McpToolParam(description = "城市名称，每次只能传入一个城市，例如：北京市、上海市、青岛市") String cityName) {
        return businessSummaryCommand.getBusinessIncomeSummaryVo(year, month, day, cityName, "COUNT");
    }
}
