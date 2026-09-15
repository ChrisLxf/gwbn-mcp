package cn.net.gwbn.ai.mcp.mcp;

import cn.net.gwbn.ai.mcp.sales.api.workorder.MassOrderCompletionRateVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.MassOrderStatisticVo;
import cn.net.gwbn.ai.mcp.sales.command.workorder.MassOrderStatisticCommand;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

import java.util.List;

/**
 * 非单户故障统计查询 MCP 客户端
 *
 * MassOrder:
 * 非单户故障, 即一次故障影响多个用户的故障.
 *
 * @author lixiaofeng
 * @date 9/14/26 PM4:30
 **/
public class MassOrderStatisticMcpClient {

    private final MassOrderStatisticCommand massOrderStatisticCommand;

    public MassOrderStatisticMcpClient(MassOrderStatisticCommand massOrderStatisticCommand) {
        this.massOrderStatisticCommand = massOrderStatisticCommand;
    }

    /**
     * 非单户故障统计查询.
     *
     * 查询指定日期非单户故障数量,
     * 同时返回当日数量和当月累计数量.
     *
     * @param year       年
     * @param month      月
     * @param day        日
     * @param cityNames  城市名称列表
     * @param finishType 工单状态
     * @return 非单户故障统计结果
     */
    @McpTool(
            name = "getMassOrderStatistic",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期非单户故障统计。

                    非单户故障说明：
                    非单户故障简称“非单”，指一次故障影响多个用户的故障。
                    MassOrder 表示非单户故障工单。

                    统计维度：
                    - 故障类型
                    - 城市

                    返回内容：
                    - 故障类型
                    - 城市ID
                    - 城市名称
                    - 当日非单户故障数量
                    - 当月累计非单户故障数量

                    调用场景：
                    - 查询全国非单户故障数量
                    - 查询指定城市非单户故障数量
                    - 查询非单户故障类型统计
                    - 查询城市非单户故障对比
                    - 查询当日非单户故障数量
                    - 查询当月累计非单户故障数量
                    - 查询已完成非单户故障数量
                    - 查询进行中非单户故障数量
                    - 查询全部状态非单户故障数量

                    城市参数：
                    - 不指定城市时，查询全部城市
                    - 指定一个城市时，查询指定城市
                    - 指定多个城市时，查询多个城市

                    故障类型包括：
                    - 线路
                    - 电力
                    - 光衰
                    - 无光
                    - 其他

                    工单状态：
                    - finishType = 0：全部非单户故障, 不限制工单状态
                    - finishType = 1：进行中非单户故障, order_state = RUNNING
                    - finishType = 2：已完成非单户故障, order_state = COMPLETED
                    """
    )
    public MassOrderStatisticVo getMassOrderStatistic(
            @McpToolParam(description = "统计年份, 例如：2026") int year,
            @McpToolParam(description = "统计月份, 取值范围：1~12, 例如：9") int month,
            @McpToolParam(description = "统计日期, 取值范围：1~31, 例如：14") int day,
            @McpToolParam(description = "城市名称列表, 例如：[北京市, 上海市]。不指定城市表示查询全部城市") List<String> cityNames,
            @McpToolParam(
                    description = """
                            非单户故障工单状态过滤类型：
                            0：全部非单户故障, 不限制工单状态
                            1：进行中非单户故障, 只统计 order_state=RUNNING
                            2：已完成非单户故障, 只统计 order_state=COMPLETED
                            例如：查询已完成非单户故障时传 2
                            """
            ) int finishType) {

        return massOrderStatisticCommand.statistic(year, month, day, cityNames, finishType);
    }

    /**
     * 非单户故障完成率统计.
     *
     * 查询指定日期非单户故障完成率,
     * 同时返回当日完成率和当月累计完成率.
     *
     * 完成率定义:
     *
     * 已完成非单户故障数量 / 全部非单户故障数量 * 100%
     *
     * @param year      年
     * @param month     月
     * @param day       日
     * @param cityNames 城市名称列表
     * @return 非单户故障完成率统计结果
     */
    @McpTool(
            name = "getMassOrderCompletionRate",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询非单户故障完成率统计。

                    非单户故障说明：
                    非单户故障简称“非单”, 指一次故障影响多个用户的故障。
                    MassOrder 表示非单户故障工单。

                    完成率定义：
                    已完成非单户故障数量 / 全部非单户故障数量 * 100%。

                    统计维度：
                    - 故障类型
                    - 城市

                    返回内容：
                    - 故障类型
                    - 城市ID
                    - 城市名称
                    - 当日全部非单户故障数量
                    - 当日已完成非单户故障数量
                    - 当日完成率
                    - 当月累计全部非单户故障数量
                    - 当月累计已完成非单户故障数量
                    - 当月累计完成率

                    调用场景：
                    - 查询全国非单户故障完成率
                    - 查询指定城市非单户故障完成率
                    - 查询各城市非单户故障完成率
                    - 查询非单户故障类型完成率
                    - 查询当日非单户故障完成率
                    - 查询当月累计非单户故障完成率
                    - 查询非单户故障处理完成情况
                    - 对比各城市非单户故障完成率
                    - 查询非单户故障处理效率

                    城市参数：
                    - 不指定城市时，查询全部城市
                    - 指定一个城市时，查询指定城市
                    - 指定多个城市时，查询多个城市

                    日期参数：
                    - day > 0：查询指定日期的当日完成率，同时返回当月累计完成率
                    - day = 0：查询整月完成率

                    完成率计算：
                    - 分子：order_state = COMPLETED 的非单户故障数量
                    - 分母：不限制 order_state 的全部非单户故障数量
                    - 完成率 = 已完成数量 / 全部数量 * 100%
                    - 完成率保留2位小数
                    - 当全部非单户故障数量为0时，完成率返回0

                    重要说明：
                    - 完成率按照非单户故障工单数量计算
                    - 不是按照城市完成率简单平均计算
                    - 每个故障类型、每个城市分别计算完成率
                    - 不需要传 finishType 参数
                    """
    )
    public MassOrderCompletionRateVo getMassOrderCompletionRate(
            @McpToolParam(description = "统计年份, 例如：2026") int year,
            @McpToolParam(description = "统计月份, 取值范围：1~12, 例如：9") int month,
            @McpToolParam(description = "统计日期, 取值范围：1~31。day > 0 查询指定日期, day = 0 查询整月, 例如：15") int day,
            @McpToolParam(description = "城市名称列表, 例如：[北京市, 上海市, 广州市]。不指定城市表示查询全部城市") List<String> cityNames) {

        return massOrderStatisticCommand.completionRate(year, month, day, cityNames);
    }
}