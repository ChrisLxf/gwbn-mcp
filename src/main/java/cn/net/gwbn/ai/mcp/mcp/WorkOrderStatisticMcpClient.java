package cn.net.gwbn.ai.mcp.mcp;

import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderCompletionRateVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderStatisticVo;
import cn.net.gwbn.ai.mcp.sales.command.workorder.WorkOrderStatisticCommand;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

import java.util.List;

/**
 * 工单统计 MCP 客户端
 *
 * @author lixiaofeng
 * @date 9/14/26 PM1:34
 **/
public class WorkOrderStatisticMcpClient {

    private final WorkOrderStatisticCommand workOrderStatisticCommand;

    public WorkOrderStatisticMcpClient(
            WorkOrderStatisticCommand workOrderStatisticCommand) {
        this.workOrderStatisticCommand = workOrderStatisticCommand;
    }

    /**
     * 工单统计查询.
     *
     * 查询指定日期的工单统计数据,
     * 同时返回当日工单数量和当月累计工单数量.
     *
     * @param year       年
     * @param month      月
     * @param day        日
     * @param metric     统计指标
     * @param cityNames  城市名称列表
     * @param finishType 工单完成状态
     * @return 工单统计结果
     */
    @McpTool(
            name = "getWorkOrderStatistic",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期工单统计数据。

                    统计维度：
                    - 工单类型
                    - 城市

                    返回内容：
                    - 工单类型
                    - 城市ID
                    - 城市名称
                    - 当日工单数量
                    - 当月累计工单数量

                    调用场景：
                    - 查询全国工单数量
                    - 查询指定城市工单数量
                    - 查询工单类型统计
                    - 查询城市工单对比
                    - 查询当日工单数量
                    - 查询当月累计工单数量
                    - 查询已完成工单数量
                    - 查询进行中工单数量
                    - 查询全部状态工单数量

                    城市参数：
                    - 不指定城市时，查询所有城市
                    - 指定一个城市时，查询该城市
                    - 指定多个城市时，查询多个城市

                    统计指标：
                    - COUNT：工单数量

                    工单状态：
                    - finishType = 0：全部工单，不限制工单状态
                    - finishType = 1：进行中工单，order_state = RUNNING
                    - finishType = 2：已完成工单，order_state = COMPLETED
                    """
    )
    public WorkOrderStatisticVo getWorkOrderStatistic(
            @McpToolParam(description = "统计年份，例如：2026") int year,
            @McpToolParam(description = "统计月份，取值范围：1~12，例如：9") int month,
            @McpToolParam(description = "统计日期，取值范围：1~31，例如：14") int day,
            @McpToolParam(description = "统计指标，可选值：COUNT，例如：COUNT") String metric,
            @McpToolParam(description = "城市名称列表，例如：[北京市, 上海市, 广州市]。不指定城市表示查询全部城市") List<String> cityNames,
            @McpToolParam(description = "工单状态过滤类型：0：全部工单，不限制工单状态；1：进行中工单，只统计 order_state=RUNNING；2：已完成工单，只统计 order_state=COMPLETED。例如：查询已完成工单时传 2") int finishType) {

        return workOrderStatisticCommand.statistic(year, month, day, metric, cityNames, finishType);
    }

    /**
     * 工单完成率统计.
     *
     * 完成率 = 已完成工单数量 / 全部工单数量 * 100%.
     *
     * 同时统计:
     * - 当日完成率
     * - 当月完成率
     *
     * @param year      年
     * @param month     月
     * @param day       日
     * @param cityNames 城市名称列表
     * @return 工单完成率统计结果
     */
    @McpTool(
            name = "getWorkOrderCompletionRate",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询工单完成率统计数据。

                    完成率定义：
                    工单完成率 = 已完成工单数量 / 全部工单数量 * 100%。

                    统计维度：
                    - 工单类型
                    - 城市

                    返回内容：
                    - 工单类型
                    - 城市ID
                    - 城市名称
                    - 当日全部工单数量
                    - 当日已完成工单数量
                    - 当日完成率
                    - 当月累计全部工单数量
                    - 当月累计已完成工单数量
                    - 当月累计完成率

                    调用场景：
                    - 查询全国工单完成率
                    - 查询指定城市工单完成率
                    - 查询各城市工单完成率
                    - 查询当日工单完成率
                    - 查询当月工单完成率
                    - 查询工单完成情况
                    - 查询工单处理效率
                    - 对比各城市工单完成率
                    - 查询已完成工单占比

                    城市参数：
                    - 不指定城市时，查询全部城市
                    - 指定一个城市时，查询该城市
                    - 指定多个城市时，查询指定城市

                    日期参数：
                    - day > 0：查询指定日期的当日完成率，同时返回当月累计完成率
                    - day = 0：查询整月完成率

                    完成率计算：
                    - 分子：order_state = COMPLETED 的工单数量
                    - 分母：不限制 order_state 的全部工单数量
                    - 完成率 = 已完成数量 / 全部数量 * 100%
                    - 完成率保留2位小数

                    重要说明：
                    - 完成率按照工单数量计算
                    - 不是按照城市完成率简单平均计算
                    - 当全部工单数量为0时，完成率返回0

                    示例：
                    某城市全部工单100个，
                    已完成80个，
                    完成率为80.00%。
                    """
    )
    public WorkOrderCompletionRateVo getWorkOrderCompletionRate(
            @McpToolParam(description = "统计年份，例如：2026") int year,
            @McpToolParam(description = "统计月份，取值范围：1~12，例如：9") int month,
            @McpToolParam(description = "统计日期，取值范围：1~31。day > 0 查询指定日期，day = 0 查询整月，例如：15") int day,
            @McpToolParam(description = "城市名称列表，例如：[北京市, 上海市, 广州市]。不指定城市表示查询全部城市") List<String> cityNames) {

        return workOrderStatisticCommand.completionRate(year, month, day, cityNames);
    }
}