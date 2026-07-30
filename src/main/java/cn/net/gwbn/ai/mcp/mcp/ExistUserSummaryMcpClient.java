package cn.net.gwbn.ai.mcp.mcp;

import cn.net.gwbn.ai.mcp.sales.api.user.ExistUserSummaryVo;
import cn.net.gwbn.ai.mcp.sales.command.ExistUserSummaryCommand;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

import java.util.List;

/**
 * 在网用户统计 MCP Tool
 *
 * @author lixiaofeng
 * @date 7/28/26 PM1:56
 */
public class ExistUserSummaryMcpClient {


    private final ExistUserSummaryCommand existUserSummaryCommand;


    public ExistUserSummaryMcpClient(ExistUserSummaryCommand existUserSummaryCommand) {
        this.existUserSummaryCommand = existUserSummaryCommand;
    }


    /**
     * 查询历史月份所有城市月初在网用户
     */
    @McpTool(
            name = "getAllExistUserSummary",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定月份所有城市月初快照在网用户统计。
                    
                    适用场景：
                    - 查询历史月份用户规模
                    - 查询某个月份期初用户数量
                    - 历史用户规模趋势分析
                    - 城市用户规模排名
                    
                    数据口径：
                    - 查询 user_expire_detail 月初快照数据
                    - 代表指定月份月初在网用户数量
                    
                    注意：
                    - 不包含当月新增用户
                    - 不包含当月到期后续费恢复用户
                    - 如果需要查询指定日期实时在网用户，请调用 getCurrentAllExistUserSummary
                    
                    返回内容：
                    - 统计年份
                    - 统计月份
                    - 用户总数量
                    - 用户单位
                    - 城市列表
                        - 城市ID
                        - 城市名称
                        - 城市在网用户数量
                        - 用户单位
                    """
    )
    public ExistUserSummaryVo getAllExistUserSummary(@McpToolParam(description = "统计年份，例如：2026") int year,
                                                     @McpToolParam(description = "统计月份，取值范围：1~12，例如：7") int month) {

        return existUserSummaryCommand.historyExistUserSummary(null, year, month);
    }


    /**
     * 查询历史月份指定城市月初在网用户
     */
    @McpTool(
            name = "getExistUserSummary",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定城市列表历史月份月初快照在网用户统计。
                    
                    适用场景：
                    - 查询指定城市历史用户规模
                    - 城市用户数量对比
                    - 区域用户规模分析
                    
                    数据口径：
                    - 查询 user_expire_detail 月初快照数据
                    - 返回指定月份月初在网用户数量
                    
                    注意：
                    - 查询结果不是指定日期实时用户数
                    - 不包含当月新增用户
                    - 不包含当月到期后续费恢复用户
                    - 如果查询实时用户数，请调用 getCurrentExistUserSummary
                    
                    支持：
                    - 单个城市查询
                    - 多个城市批量查询
                    
                    返回内容：
                    - 统计年份
                    - 统计月份
                    - 用户总数量
                    - 用户单位
                    - 城市列表
                        - 城市ID
                        - 城市名称
                        - 城市在网用户数量
                        - 用户单位
                    """
    )
    public ExistUserSummaryVo getExistUserSummary(
            @McpToolParam(description = """
                    城市名称列表，例如：
                    ["北京市","上海市","青岛市"]
                    支持多个城市同时查询
                    """)
            List<String> cityNames,
            @McpToolParam(description = "统计年份，例如：2026") int year,
            @McpToolParam(description = "统计月份，取值范围：1~12，例如：7") int month) {


        return existUserSummaryCommand.historyExistUserSummary(cityNames, year, month);
    }


    /**
     * 查询指定日期全国实时在网用户
     */
    @McpTool(
            name = "getCurrentAllExistUserSummary",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期全国所有城市实时在网用户统计。
                    
                    适用场景：
                    - 查询当天全国在网用户规模
                    - 查询实时用户数量
                    - 当前经营分析
                    - 用户规模监控
                    
                    数据口径：
                    实时在网用户 =
                    月初快照中截止指定日期仍有效用户
                    +
                    当月到期后续费恢复用户
                    +
                    当月新装用户
                    
                    数据来源：
                    - user_expire_detail 月初快照
                    - user_order_statistic 当月续费记录
                    - user_order_statistic 当月新装记录
                    
                    注意：
                    - 该接口查询指定日期实时状态
                    - 查询历史月份月初用户，请调用 getAllExistUserSummary
                    
                    返回内容：
                    - 统计年份
                    - 统计月份
                    - 统计日期
                    - 用户总数量
                    - 用户单位
                    - 城市列表
                        - 城市ID
                        - 城市名称
                        - 城市在网用户数量
                        - 用户单位
                    """
    )
    public ExistUserSummaryVo getCurrentAllExistUserSummary(
            @McpToolParam(description = "统计年份，例如：2026") int year,
            @McpToolParam(description = "统计月份，例如：7")
            int month,
            @McpToolParam(description = "统计日期，例如：27") int day) {

        return existUserSummaryCommand.currentExistUserSummary(year, month, day, null);
    }


    /**
     * 查询指定日期指定城市实时在网用户
     */
    @McpTool(
            name = "getCurrentExistUserSummary",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期指定城市实时在网用户统计。
                    
                    适用场景：
                    - 查询某些城市当天用户规模
                    - 城市用户规模比较
                    - 区域实时经营分析
                    
                    数据口径：
                    实时在网用户 =
                    月初快照有效用户
                    +
                    当月到期后续费恢复用户
                    +
                    当月新装用户
                    
                    支持：
                    - 单个城市查询
                    - 多个城市批量查询
                    
                    注意：
                    - 查询历史月份用户规模，请调用 getExistUserSummary
                    - 查询全国实时用户，请调用 getCurrentAllExistUserSummary
                    
                    返回内容：
                    - 统计年份
                    - 统计月份
                    - 统计日期
                    - 用户总数量
                    - 用户单位
                    - 城市列表
                        - 城市ID
                        - 城市名称
                        - 城市在网用户数量
                        - 用户单位
                    """
    )
    public ExistUserSummaryVo getCurrentExistUserSummary(
            @McpToolParam(description = """
                    城市名称列表，例如：
                    ["北京市","上海市","青岛市"]
                    支持多个城市同时查询
                    """)
            List<String> cityNames,
            @McpToolParam(description = "统计年份，例如：2026") int year,
            @McpToolParam(description = "统计月份，例如：7") int month,
            @McpToolParam(description = "统计日期，例如：27") int day) {


        return existUserSummaryCommand.currentExistUserSummary(year, month, day, cityNames);
    }
}