package cn.net.gwbn.ai.mcp.mcp;

import cn.net.gwbn.ai.mcp.sales.api.business.BusinessSummaryVo;
import cn.net.gwbn.ai.mcp.sales.api.product.ProductSummaryVo;
import cn.net.gwbn.ai.mcp.sales.command.ProductSummaryCommand;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

/**
 * @author lixiaofeng
 * @date 6/29/26 AM9:18
 **/
public class ProductSummaryMcpClient {


    private final ProductSummaryCommand productSummaryCommand;

    public ProductSummaryMcpClient(ProductSummaryCommand productSummaryCommand) {
        this.productSummaryCommand = productSummaryCommand;
    }


    @McpTool(name = "getAllCityProductIncome",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期全国所有城市按产品规格维度统计的收入。
                    
                    调用场景：
                    - 用户查询全国产品收入
                    - 用户分析产品收入结构
                    - 用户比较不同产品规格收入
                    - 用户分析带宽、时长或价格区间收入
                    
                    返回内容：
                    - 每个产品规格的当日收入
                    - 每个产品规格的当月累计收入
                    
                    """
    )
    public ProductSummaryVo getAllCityProductIncome(@McpToolParam(description = "统计年份，例如：2026") int year,
                                                    @McpToolParam(description = "统计月份，取值范围：1~12，例如：5") int month,
                                                    @McpToolParam(description = "统计日期，取值范围：1~31，例如：29") int day,
                                                    @McpToolParam(description = "统计维度，一次只能选择一个维度。，可选值：带宽、时长、价格区间，例如：带宽") String dimension,
                                                    @McpToolParam(description = "统计类别，可选值:自营、代管，每次只能传一个值，例如：自营") String type) {
        return productSummaryCommand.getAllCityProductSummaryVo(year, month, day, type, dimension, "INCOME");
    }


    @McpTool(name = "getAllCityProductCount",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期全国所有城市按产品规格维度统计的办理数量。
                    
                    调用场景：
                    - 用户查询全国产品办理数量
                    - 用户分析产品结构
                    - 用户比较不同产品规格办理量
                    - 用户分析带宽、时长或价格区间办理情况
                    
                    返回内容：
                    - 每个产品规格的当日办理数量
                    - 每个产品规格的当月累计办理数量
                    """
    )
    public ProductSummaryVo getAllCityProductCount(@McpToolParam(description = "统计年份，例如：2026") int year,
                                                   @McpToolParam(description = "统计月份，取值范围：1~12，例如：5") int month,
                                                   @McpToolParam(description = "统计日期，取值范围：1~31，例如：29") int day,
                                                   @McpToolParam(description = "统计维度，一次只能选择一个维度。，可选值：带宽、时长、价格区间，例如：带宽") String dimension,
                                                   @McpToolParam(description = "统计类别，可选值:自营、代管，每次只能传一个值，例如：自营") String type) {
        return productSummaryCommand.getAllCityProductSummaryVo(year, month, day, type, dimension, "COUNT");
    }

    @McpTool(name = "getCityProductIncome",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期某个城市按产品规格维度统计的收入。
                    
                    调用场景：
                    - 用户查询某城市产品收入
                    - 用户分析某城市产品收入结构
                    - 用户比较某城市不同产品规格收入
                    
                    返回内容：
                    - 每个产品规格的当日收入
                    - 每个产品规格的当月累计收入
                    
                    """
    )
    public ProductSummaryVo getCityProductIncome(@McpToolParam(description = "统计年份，例如：2026") int year,
                                                 @McpToolParam(description = "统计月份，取值范围：1~12，例如：5") int month,
                                                 @McpToolParam(description = "统计日期，取值范围：1~31，例如：29") int day,
                                                 @McpToolParam(description = "城市名称，每次只能传入一个城市，例如：北京市、上海市、青岛市") String cityName,
                                                 @McpToolParam(description = "统计维度，一次只能选择一个维度。，可选值：带宽、时长、价格区间，例如：带宽") String dimension) {
        return productSummaryCommand.getProductSummaryVo(year, month, day, cityName, dimension, "INCOME");
    }


    @McpTool(name = "getCityProductCount",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期某个城市按产品规格维度统计的办理数量。
                    
                    调用场景：
                    - 用户查询某城市产品办理数量
                    - 用户分析某城市产品结构
                    - 用户比较某城市不同产品规格办理量
                    
                    返回内容：
                    - 每个产品规格的当日办理数量
                    - 每个产品规格的当月累计办理数量
                    """
    )
    public ProductSummaryVo getCityProductCount(@McpToolParam(description = "统计年份，例如：2026") int year,
                                                @McpToolParam(description = "统计月份，取值范围：1~12，例如：5") int month,
                                                @McpToolParam(description = "统计日期，取值范围：1~31，例如：29") int day,
                                                @McpToolParam(description = "城市名称，每次只能传入一个城市，例如：北京市、上海市、青岛市") String cityName,
                                                @McpToolParam(description = "统计维度，一次只能选择一个维度。，可选值：带宽、时长、价格区间，例如：带宽") String dimension) {
        return productSummaryCommand.getProductSummaryVo(year, month, day, cityName, dimension, "COUNT");
    }
}
