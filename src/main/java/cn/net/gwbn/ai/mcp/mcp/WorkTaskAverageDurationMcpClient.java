package cn.net.gwbn.ai.mcp.mcp;

import cn.net.gwbn.ai.mcp.sales.api.workorder.WorTaskAverageDurationVo;
import cn.net.gwbn.ai.mcp.sales.command.workorder.WorTaskAverageDurationCommand;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

import java.util.Arrays;
import java.util.List;

/**
 * 非单工单一线施工环节平均完成时长 MCP.
 *
 * @author lixiaofeng
 * @date 9/15/26
 */
public class WorkTaskAverageDurationMcpClient {

    /**
     * 非单工单类型.
     */
    private static final String MASS_ORDER_TYPE = "非单";

    /**
     * 非单工单一线施工任务.
     */
    private static final List<String> TASK_NAMES = Arrays.asList("部门施工", "上门施工");

    private final WorTaskAverageDurationCommand command;

    public WorkTaskAverageDurationMcpClient(
            WorTaskAverageDurationCommand command) {

        this.command = command;
    }

    @McpTool(
            name = "getMassOrderConstructionAverageDuration",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询非单户故障工单一线施工环节的平均完成时长。

                    【业务概念】
                    非单户故障简称"非单", 指一次故障影响多个用户的故障。
                    MassOrder 表示非单户故障工单。

                    本工具用于统计非单工单在一线施工环节的处理效率，
                    重点反映一线人员从施工任务开始处理到任务完成的平均耗时。

                    【适用场景】
                    当用户询问以下业务场景的平均完成时长、平均处理时长、
                    平均作业时长或处理效率时, 可以使用本工具：
                    - 非单一线施工
                    - 非单一线处理
                    - 非单施工处理
                    - 非单抢修处理
                    - 非单上门施工
                    - 非单部门施工
                    - 非单工单施工
                    - 非单工单一线作业
                    - 非单故障施工效率
                    - 非单工单处理效率

                    例如：
                    - 查询今天非单工单一线施工平均需要多长时间
                    - 查询本月各城市非单工单施工平均处理时长
                    - 查询北京非单工单一线施工平均完成时长
                    - 查询全国非单工单施工处理效率
                    - 对比各城市非单工单一线施工平均完成时长
                    - 查询部门施工和上门施工的平均完成时长

                    【统计范围】
                    - 工单类型固定为"非单"
                    - 只统计非单工单的一线施工任务
                    - 当前施工任务包括：
                      - 部门施工
                      - 上门施工
                    - 只统计已完成任务
                    - order_state = COMPLETED

                    【统计维度】
                    - 工单类型
                    - 城市
                    - 施工任务

                    【返回数据】
                    - 工单类型
                    - 城市ID
                    - 城市名称
                    - 任务名称
                    - 当日平均完成时长
                    - 当月平均完成时长

                    【平均完成时长】
                    - duration 为任务处理持续时间
                    - 数据库 duration 单位为毫秒
                    - 返回结果单位为分钟
                    - 平均完成时长保留2位小数

                    【城市查询】
                    - 不指定城市时, 查询全部城市
                    - 指定一个城市时, 查询该城市
                    - 指定多个城市时, 查询指定城市

                    【日期查询】
                    - day > 0:
                      查询指定日期的数据,
                      同时返回该月份截至当前统计范围的月平均完成时长
                    - day = 0:
                      查询整月平均完成时长

                    【重要边界】
                    本工具只统计"非单"工单中的"部门施工"和"上门施工"任务。
                    如果用户询问其他工单类型、其他任务类型或非施工环节，
                    不应使用本工具。

                    用户使用"一线施工"、"一线处理"、"抢修处理"、
                    "施工处理"等业务描述时, 如果上下文明确指向
                    非单工单施工环节, 应使用本工具进行统计。
                    """
    )
    public WorTaskAverageDurationVo query(
            @McpToolParam(description = "统计年份, 例如：2026") int year,
            @McpToolParam(description = "统计月份, 取值范围：1~12, 例如：9") int month,
            @McpToolParam(description = "统计日期, 取值范围：1~31。day > 0 查询指定日期, day = 0 查询整月, 例如：15") int day,
            @McpToolParam(description = "城市名称列表。为空表示查询全部城市, 例如：[北京市, 上海市, 广州市]") List<String> cityNames) {

        return command.statistic(year, month, day, MASS_ORDER_TYPE, cityNames, TASK_NAMES);
    }
}