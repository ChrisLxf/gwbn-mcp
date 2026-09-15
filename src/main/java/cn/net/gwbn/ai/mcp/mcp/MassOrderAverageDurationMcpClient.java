package cn.net.gwbn.ai.mcp.mcp;

import cn.net.gwbn.ai.mcp.sales.api.workorder.MassOrderAverageDurationVo;
import cn.net.gwbn.ai.mcp.sales.command.workorder.MassOrderAverageDurationCommand;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

import java.util.List;

/**
 * 非单户故障平均完成时长统计 MCP 客户端.
 *
 * @author lixiaofeng
 * @date 9/14/26
 **/
public class MassOrderAverageDurationMcpClient {

    private final MassOrderAverageDurationCommand
            massOrderAverageDurationCommand;

    public MassOrderAverageDurationMcpClient(MassOrderAverageDurationCommand massOrderAverageDurationCommand) {
        this.massOrderAverageDurationCommand = massOrderAverageDurationCommand;
    }

    /**
     * 非单户故障平均完成时长统计.
     *
     * 查询指定日期非单户故障平均完成时长,
     * 同时返回当日平均完成时长和当月平均完成时长.
     */
    @McpTool(
            name = "getMassOrderAverageDuration",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期非单户故障平均完成时长。

                    非单户故障说明：
                    非单户故障简称“非单”, 指一次故障影响多个用户的故障。
                    MassOrder 表示非单户故障工单。

                    统计维度：
                    - 故障类型
                    - 城市

                    返回内容：
                    - 故障类型
                    - 城市ID
                    - 城市名称
                    - 当日平均完成时长
                    - 当月平均完成时长

                    统计规则：
                    - 只统计已完成的非单户故障
                    - order_state = COMPLETED
                    - 根据 duration 字段计算平均完成时长

                    平均完成时长单位：
                    - 分钟

                    故障类型包括：
                    - 线路
                    - 电力
                    - 光衰
                    - 无光
                    - 其他

                    调用场景：
                    - 查询全国非单户故障平均完成时长
                    - 查询指定城市非单户故障平均完成时长
                    - 查询非单户故障类型平均完成时长
                    - 查询城市非单户故障平均完成时长对比
                    - 查询当日非单户故障平均完成时长
                    - 查询当月非单户故障平均完成时长

                    城市参数：
                    - 不指定城市时, 查询全部城市
                    - 指定一个城市时, 查询指定城市
                    - 指定多个城市时, 查询多个城市

                    日期说明：
                    - day > 0：查询指定日期的平均完成时长
                    - day = 0：查询整月平均完成时长
                    """
    )
    public MassOrderAverageDurationVo getMassOrderAverageDuration(
            @McpToolParam(description = "统计年份, 例如：2026") int year,
            @McpToolParam(description = "统计月份, 取值范围：1~12, 例如：9") int month,
            @McpToolParam(description = "统计日期, 取值范围：1~31, 例如：14") int day,
            @McpToolParam(description = "城市名称列表, 例如：[北京市, 上海市, 广州市]。不指定城市表示查询全部城市") List<String> cityNames) {

        return massOrderAverageDurationCommand.statistic(year, month, day, cityNames);
    }
}