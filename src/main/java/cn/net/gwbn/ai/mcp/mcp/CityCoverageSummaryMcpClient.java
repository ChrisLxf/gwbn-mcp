package cn.net.gwbn.ai.mcp.mcp;

import cn.net.gwbn.ai.mcp.sales.api.coverage.CityCoverageSummaryVo;
import cn.net.gwbn.ai.mcp.sales.command.CityCoverageSummaryCommand;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

import java.util.List;

/**
 * @author lixiaofeng
 * @date 7/30/26 PM3:57
 **/
public class CityCoverageSummaryMcpClient {


    private final CityCoverageSummaryCommand cityCoverageSummaryCommand;


    public CityCoverageSummaryMcpClient(CityCoverageSummaryCommand cityCoverageSummaryCommand) {
        this.cityCoverageSummaryCommand = cityCoverageSummaryCommand;
    }



    /**
     * 查询所有城市覆盖用户数量
     */
    @McpTool(
            name = "getAllCityCoverageSummary",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询所有城市宽带覆盖用户数量统计。

                    适用场景：
                    - 查询全国城市覆盖用户数量
                    - 查询城市覆盖规模排名
                    - 分析城市覆盖能力
                    - 查询区域覆盖规模

                    数据口径：
                    - 统计 city_coverage 覆盖数据
                    - 返回城市覆盖用户数量

                    返回内容：
                    - 覆盖用户总数量
                    - 用户单位
                    - 城市覆盖列表
                        - 城市ID
                        - 城市名称
                        - 覆盖用户数量
                        - 用户单位

                    注意：
                    - cityNames为空时查询全部城市
                    - 支持城市覆盖规模比较
                    """
    )
    public CityCoverageSummaryVo getAllCityCoverageSummary() {
        return cityCoverageSummaryCommand.execute(null);
    }



    /**
     * 查询指定城市覆盖用户数量
     */
    @McpTool(
            name = "getCityCoverageSummary",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定城市列表宽带覆盖用户数量统计。

                    适用场景：
                    - 查询某个城市覆盖用户数量
                    - 多个城市覆盖规模比较
                    - 区域覆盖分析

                    支持：
                    - 单城市查询
                    - 多城市批量查询

                    返回内容：
                    - 覆盖用户总数量
                    - 用户单位
                    - 城市覆盖列表
                        - 城市ID
                        - 城市名称
                        - 覆盖用户数量
                        - 用户单位

                    注意：
                    - 传入城市名称列表
                    - 不传城市列表时请调用 getAllCityCoverageSummary
                    """
    )
    public CityCoverageSummaryVo getCityCoverageSummary(
            @McpToolParam(description = """
                    城市名称列表。

                    示例：
                    ["北京市","上海市","青岛市"]

                    支持多个城市同时查询。
                    """)
            List<String> cityNames) {


        return cityCoverageSummaryCommand.execute(cityNames);
    }
}