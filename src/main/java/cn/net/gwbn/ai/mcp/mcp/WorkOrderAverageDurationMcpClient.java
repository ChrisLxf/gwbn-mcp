package cn.net.gwbn.ai.mcp.mcp;

import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderAverageDurationVo;
import cn.net.gwbn.ai.mcp.sales.command.workorder.WorkOrderAverageDurationCommand;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

import java.util.List;

/**
 * 工单平均完成时长统计 MCP 客户端.
 *
 * @author lixiaofeng
 * @date 9/14/26
 **/
public class WorkOrderAverageDurationMcpClient {

    private final WorkOrderAverageDurationCommand workOrderAverageDurationCommand;

    public WorkOrderAverageDurationMcpClient(
            WorkOrderAverageDurationCommand workOrderAverageDurationCommand) {

        this.workOrderAverageDurationCommand =
                workOrderAverageDurationCommand;
    }

    /**
     * 工单平均完成时长统计.
     * <p>
     * 查询指定日期工单平均完成时长,
     * 同时返回当日平均完成时长和当月平均完成时长.
     */
    @McpTool(
            name = "getWorkOrderAverageDuration",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期工单平均完成时长。
                    
                    统计维度：
                    - 工单类型
                    - 城市
                    
                    返回内容：
                    - 工单类型
                    - 城市ID
                    - 城市名称
                    - 当日平均完成时长
                    - 当月平均完成时长
                    
                    统计规则：
                    - 只统计已完成工单
                    - order_state = COMPLETED
                    - 根据 duration 字段计算平均完成时长
                    
                    平均完成时长单位：
                    - 分钟
                    
                    调用场景：
                    - 查询全国工单平均完成时长
                    - 查询指定城市工单平均完成时长
                    - 查询工单类型平均完成时长
                    - 查询城市平均完成时长对比
                    - 查询当日平均完成时长
                    - 查询当月平均完成时长
                    
                    城市参数：
                    - 不指定城市时，查询全部城市
                    - 指定一个城市时，查询该城市
                    - 指定多个城市时，查询多个城市
                    
                    日期说明：
                    - day > 0：查询指定日期的平均完成时长
                    - day = 0：查询整月平均完成时长
                    """
    )
    public WorkOrderAverageDurationVo getWorkOrderAverageDuration(
            @McpToolParam(description = "统计年份, 例如：2026")
            int year,

            @McpToolParam(description = "统计月份, 取值范围：1~12, 例如：9")
            int month,

            @McpToolParam(
                    description = "统计日期, 取值范围：1~31, 例如：14"
            )
            int day,

            @McpToolParam(
                    description = "城市名称列表, 例如：[北京市, 上海市, 广州市]。不指定城市表示查询全部城市"
            )
            List<String> cityNames) {

        return workOrderAverageDurationCommand.statistic(year, month, day, cityNames);
    }
}