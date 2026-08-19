package cn.net.gwbn.ai.mcp.mcp;

import cn.net.gwbn.ai.mcp.sales.api.user.ActiveUserSummaryVo;
import cn.net.gwbn.ai.mcp.sales.command.ActiveUserSummaryCommand;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

import java.util.List;

/**
 * @author lixiaofeng
 * @date 8/19/26 PM2:10
 **/
public class ActiveUserSummaryMcpClient {


    private final ActiveUserSummaryCommand activeUserSummaryCommand;


    public ActiveUserSummaryMcpClient(ActiveUserSummaryCommand activeUserSummaryCommand) {
        this.activeUserSummaryCommand = activeUserSummaryCommand;
    }


    /**
     * 查询所有城市近三个月活跃用户
     */
    @McpTool(
            name = "getAllActiveUserSummary",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定月份全国所有城市活跃用户统计。
                    
                    适用场景：
                    - 查询城市活跃用户规模
                    - 查询用户上网活跃情况
                    - 城市活跃用户排名
                    - 用户经营分析
                    
                    数据口径：
                    活跃用户 =
                    指定月份的在上网记录的去重用户。
                    
                    例如：
                    查询2026年8月活跃用户，
                    统计周期为：
                    2026年6月、7月、8月
                    
                    数据来源：
                    - active_user_statistic 用户上网行为统计表
                    
                    注意：
                    - 该指标不是当前在网用户数量
                    - 查询在网用户请调用 getAllExistUserSummary
                    
                    返回内容：
                    - 用户总数量
                    - 用户单位
                    - 城市列表
                        - 城市ID
                        - 城市名称
                        - 城市活跃用户数量
                        - 用户单位
                    """
    )
    public ActiveUserSummaryVo getAllActiveUserSummary(
            @McpToolParam(description = "统计年份，例如：2026") int year,
            @McpToolParam(description = "统计月份，取值范围：1~12，例如：8") int month) {
        return activeUserSummaryCommand.getCityActiveUserCount(null, year, month);
    }



    /**
     * 查询指定城市近三个月活跃用户
     */
    @McpTool(
            name = "getActiveUserSummary",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定城市列表指定月的活跃用户统计。
                    
                    适用场景：
                    - 查询指定城市活跃用户规模
                    - 城市活跃用户对比
                    - 区域用户活跃度分析
                    
                    数据口径：
                    活跃用户 =
                    指定月份的在上网记录的去重用户。
                    
                    示例：
                    查询2026年8月，
                    返回2026年6月至2026年8月期间产生过上网记录的用户数量。
                    
                    支持：
                    - 单个城市查询
                    - 多个城市批量查询
                    
                    注意：
                    - 返回的是去重用户数量
                    - 不代表当前在线用户
                    - 不代表月初在网用户
                    
                    返回内容：
                    - 用户总数量
                    - 用户单位
                    - 城市列表
                        - 城市ID
                        - 城市名称
                        - 城市活跃用户数量
                        - 用户单位
                    """
    )
    public ActiveUserSummaryVo getActiveUserSummary(
            @McpToolParam(description = "城市名称列表，例如：\n[\"北京市\",\"上海市\",\"青岛市\"]\n\n支持多个城市同时查询\n") List<String> cityNames,
            @McpToolParam(description = "统计年份，例如：2026") int year,
            @McpToolParam(description = "统计月份，取值范围：1~12，例如：8") int month) {
        return activeUserSummaryCommand.getCityActiveUserCount(cityNames, year, month);
    }

}