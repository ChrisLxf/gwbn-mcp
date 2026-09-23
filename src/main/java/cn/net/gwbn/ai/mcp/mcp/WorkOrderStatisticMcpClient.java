
package cn.net.gwbn.ai.mcp.mcp;

import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderStatisticVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkOrderTypeStatisticVo;
import cn.net.gwbn.ai.mcp.sales.command.workorder.WorkOrderStatisticCommand;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

import java.util.List;

/**
 * 工单统计 MCP 客户端.
 *
 * @author lixiaofeng
 * @date 9/14/26 PM1:34
 */
public class WorkOrderStatisticMcpClient {

    private final WorkOrderStatisticCommand workOrderStatisticCommand;

    public WorkOrderStatisticMcpClient(WorkOrderStatisticCommand workOrderStatisticCommand) {
        this.workOrderStatisticCommand = workOrderStatisticCommand;
    }

    /**
     * 查询全部工单.
     */
    @McpTool(
            name = "getAllWorkOrderStatistic",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期创建的全部工单数量。
                    
                    统计口径：
                    - 按工单创建时间统计。
                    - 不限制工单状态。
                    - 包含进行中、已完成等全部工单。
                    
                    统计维度：
                    - 工单类型
                    - 城市
                    
                    返回内容：
                    - 工单类型
                    - 城市ID
                    - 城市名称
                    - 当日工单数量
                    - 当月累计工单数量
                    
                    城市参数：
                    - 不指定城市时，查询全部城市。
                    - 指定一个城市时，查询该城市。
                    - 指定多个城市时，查询指定城市。
                    
                    """
    )
    public WorkOrderStatisticVo getAllWorkOrderStatistic(
            @McpToolParam(description = "统计年份，例如：2026") int year,
            @McpToolParam(description = "统计月份，取值范围：1~12，例如：9") int month,
            @McpToolParam(description = "统计日期，取值范围：0~31。day > 0 查询指定日期，，例如：15") int day,
            @McpToolParam(description = "城市名称列表，例如：[北京市, 上海市, 广州市]。不指定城市表示查询全部城市") List<String> cityNames) {

        return workOrderStatisticCommand.orderStatistic(year, month, day, "全部工单统计", cityNames, 0, false, false);
    }

    /**
     * 查询进行中工单.
     */
    @McpTool(
            name = "getRunningWorkOrderStatistic",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期创建且当前仍在进行中的工单数量。
                    
                    统计口径：
                    - 按工单创建时间统计。
                    - 工单状态固定为 RUNNING。
                    
                    统计维度：
                    - 工单类型
                    - 城市
                    
                    返回内容：
                    - 工单类型
                    - 城市ID
                    - 城市名称
                    - 当日进行中工单数量
                    - 当月累计进行中工单数量
                    
                    城市参数：
                    - 不指定城市时，查询全部城市。
                    - 指定一个城市时，查询该城市。
                    - 指定多个城市时，查询指定城市。
                    
                    """
    )
    public WorkOrderStatisticVo getRunningWorkOrderStatistic(
            @McpToolParam(description = "统计年份，例如：2026") int year,
            @McpToolParam(description = "统计月份，取值范围：1~12，例如：9") int month,
            @McpToolParam(description = "统计日期，取值范围：1~31。day > 0 查询指定日期，例如：15") int day,
            @McpToolParam(description = "城市名称列表，例如：[北京市, 上海市, 广州市]。不指定城市表示查询全部城市") List<String> cityNames) {
        return workOrderStatisticCommand.orderStatistic(year, month, day, "进行中工单统计", cityNames, 1, false, false);
    }

    /**
     * 查询已完成工单.
     */
    /**
     * 查询已完成工单.
     */
    @McpTool(
            name = "getCompletedWorkOrderStatistic",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询已完成工单数量。
                    
                    
                    统计口径：
                    - 工单状态固定为 COMPLETED。
                    - 所有查询都会按照工单创建时间进行统计。
                    - useCompleteDate 用于判断是否时指定日期并且在创建指定日期完成的工单
                    
                    当 useCompleteDate = true：
                    - 指定日期创建并且在指定日期完成的工单
                    
                    当 useCompleteDate = false：
                    - 指定日期完成的额全部工单
                    
                    
                    统计维度：
                    - 工单类型
                    - 城市
                    
                    返回内容：
                    - 工单类型
                    - 城市ID
                    - 城市名称
                    - 当日完成工单数量
                    - 当月累计完成工单数量
                    
                    城市参数：
                    - 不指定城市时，查询全部城市。
                    - 指定一个城市时，只查询该城市。
                    - 指定多个城市时，只查询指定城市。
                    
                    参数使用建议：
                    - 查询“当天创建当天完成”的工单时，useCompleteDate = true。
                    - 查询“指定时间内创建且当前已经完成”的工单时，useCompleteDate = false。
                    """
    )
    public WorkOrderStatisticVo getCompletedWorkOrderStatistic(
            @McpToolParam(description = "统计年份，例如：2026") int year,
            @McpToolParam(description = "统计月份，取值范围：1~12，例如：9") int month,
            @McpToolParam(description = "统计日期，取值范围：1~31。day > 0 查询指定日期") int day,
            @McpToolParam(description = "城市名称列表，例如：[北京市, 上海市, 广州市]。不指定城市表示查询全部城市") List<String> cityNames,
            @McpToolParam(description = "是否当前创建当天完成。true=指定日期创建并且在指定日期完成的工单；false=指定日期完成的额全部工单") boolean useCompleteDate) {
        return workOrderStatisticCommand.orderStatistic(year, month, day, "完成工单统计", cityNames, 2, useCompleteDate, false);
    }


    /**
     * 统计客服完成工单数量
     *
     * @param year
     * @param month
     * @param day
     * @param cityNames
     * @param useCompleteDate
     * @return
     */
    /**
     * 统计客服完成工单数量.
     */
    @McpTool(
            name = "getKfCompletedWorkOrderStatistic",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询客服完成工单数量.
                    
                    统计口径：
                    - 工单状态固定为 COMPLETED.
                    - 仅统计客服完成的工单.
                    - 按工单创建时间进行统计.
                    - useCompleteDate 用于控制是否同时限定工单完成日期.
                    
                    当 useCompleteDate = true：
                    - 查询指定时间范围内创建, 并且在对应指定日期完成的客服工单.
                    - 当 day > 0 时, 表示指定日期创建且指定日期完成的客服工单.
                    - 当 day = 0 时, 表示指定月份内创建且在指定月份完成的客服工单.
                    
                    当 useCompleteDate = false：
                    - 查询指定时间范围内创建, 当前已经完成的客服工单.
                    - 不额外限定完成日期.
                    
                    统计维度：
                    - 工单类型
                    - 城市
                    
                    返回内容：
                    - 工单类型
                    - 城市ID
                    - 城市名称
                    - 当日客服完成工单数量
                    - 当月累计客服完成工单数量
                    
                    城市参数：
                    - 不指定城市时, 查询全部城市.
                    - 指定一个城市时, 只查询该城市.
                    - 指定多个城市时, 只查询指定城市.
                    
                    参数使用建议：
                    - 查询客服当天创建当天完成的工单时, useCompleteDate = true.
                    - 查询指定时间内创建且当前已经完成的客服工单时, useCompleteDate = false.
                    """
    )
    public WorkOrderStatisticVo getKfCompletedWorkOrderStatistic(
            @McpToolParam(description = "统计年份, 例如：2026") int year,
            @McpToolParam(description = "统计月份, 取值范围：1~12, 例如：9") int month,
            @McpToolParam(description = "统计日期, 取值范围：1~31. day > 0 查询指定日期, day = 0 查询整月") int day,
            @McpToolParam(description = "城市名称列表, 例如：[北京市, 上海市, 广州市]. 不指定城市表示查询全部城市") List<String> cityNames,
            @McpToolParam(description = "是否限定完成日期. true=限定工单在指定日期/月份内完成; false=不限定完成日期, 统计指定时间内创建且当前已经完成的工单") boolean useCompleteDate) {
        return workOrderStatisticCommand.orderStatistic(year, month, day, "完成工单统计", cityNames, 2, useCompleteDate, true);
    }


    /**
     * 增加派单用户数量统计,统计派单用户数
     *
     * @param year
     * @param month
     * @param day
     * @param cityNames
     * @return
     */
    @McpTool(
            name = "getWorkOrderUserStatistic",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询派单用户数量统计.
                    
                    用于统计指定年份、月份、日期和城市范围内的派单用户数量.
                    
                    统计口径：
                    - 统计全部工单对应的派单用户数量.
                    - 按工单创建时间统计.
                    - 不限制工单状态.
                    - 统计的是派单用户数量, 不是工单数量.
                    - 同一个用户在统计范围内是否去重, 以实际查询结果为准, 不根据工单数量推算用户数量.
                    
                    统计维度：
                    - 工单类型
                    - 城市
                    
                    返回内容：
                    - 工单类型
                    - 城市ID
                    - 城市名称
                    - 当日派单用户数量
                    - 当月累计派单用户数量
                    
                    城市参数：
                    - 不指定城市时, 查询全部城市.
                    - 指定一个城市时, 只查询该城市.
                    - 指定多个城市时, 只查询指定城市.
                    
                    """
    )
    public WorkOrderStatisticVo getWorkOrderUserStatistic(
            @McpToolParam(description = "统计年份, 例如：2026") int year,
            @McpToolParam(description = "统计月份, 取值范围：1~12, 例如：9") int month,
            @McpToolParam(description = "统计日期, 取值范围：1~31. day > 0 查询指定日期 例如：15") int day,
            @McpToolParam(description = "城市名称列表, 例如：[北京市, 上海市, 广州市]. 不指定城市表示查询全部城市") List<String> cityNames) {
        return workOrderStatisticCommand.orderUserStatistic(year, month, day, "全部工单统计", cityNames);
    }


    /**
     * 查询催单数量.
     */
    @McpTool(
            name = "getWorkOrderRemainderStatistic",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询工单催单次数统计。
                    
                    统计口径：
                    - 按工单创建时间统计。
                    - 统计 work_order_statistic.remind_times 的累计值。
                    
                    统计维度：
                    - 城市
                    - 可选工单类型
                    
                    返回内容：
                    - 工单类型
                    - 城市ID
                    - 城市名称
                    - 当日催单次数
                    - 当月累计催单次数
                    
                    城市参数：
                    - 不指定城市时，查询全部城市。
                    - 指定一个城市时，查询该城市。
                    - 指定多个城市时，查询指定城市。
                    
                    
                    """
    )
    public WorkOrderStatisticVo getWorkOrderRemainderStatistic(
            @McpToolParam(description = "统计年份，例如：2026") int year,
            @McpToolParam(description = "统计月份，取值范围：1~12，例如：9") int month,
            @McpToolParam(description = "统计日期，取值范围：0~31。day > 0 查询指定日期，例如:15") int day,
            @McpToolParam(description = "城市名称列表，例如：[北京市, 上海市, 广州市]。不指定城市表示查询全部城市") List<String> cityNames) {
        return workOrderStatisticCommand.remainderStatistic(year, month, day, cityNames);
    }

    /**
     * 重复工单用户统计.
     */
    @McpTool(
            name = "getWorkOrderDuplicateStatistic",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                     查询重复工单用户数量。
                    
                     统计口径：
                     - 按工单创建时间统计。
                     - 按 user_id 对工单进行分组。
                     - 同一个 user_id 在统计周期内创建的工单数量大于1，则认为该用户存在重复工单。
                     - 每个重复用户只统计1个，不按照重复工单数量重复计算。
                    
                    例如：
                     - 用户A创建1个工单，不属于重复用户。
                     - 用户B创建2个工单，属于1个重复用户。
                     - 用户C创建5个工单，仍属于1个重复用户。
                    
                     因此：
                     - 重复用户数量统计的是符合条件的 user_id 数量。
                     - 不是重复工单数量。
                     - 不是重复次数。
                    
                    统计维度：
                     - 工单类型
                     - 城市
                    
                     返回内容：
                     - 工单类型
                     - 城市ID
                     - 城市名称
                     - 当日重复用户数量
                     - 当月累计重复用户数量
                    
                    城市参数：
                     - 不指定城市时，查询全部城市。
                     - 指定一个城市时，只查询该城市。
                     - 指定多个城市时，只查询指定城市。
                    
                    重复判断规则：
                     - 同一个 user_id 在统计周期内工单数量 <= 1：不属于重复用户。
                     - 同一个 user_id 在统计周期内工单数量 > 1：计为1个重复用户。
                    """)
    public WorkOrderStatisticVo getWorkOrderDuplicateStatistic(
            @McpToolParam(description = "统计年份，例如：2026") int year,
            @McpToolParam(description = "统计月份，取值范围：1~12，例如：9") int month,
            @McpToolParam(description = "统计日期，取值范围：1~31。day > 0 查询指定日期，例如：15") int day,
            @McpToolParam(description = "城市名称列表，例如：[北京市, 上海市, 广州市]。不指定城市表示查询全部城市") List<String> cityNames) {
        return workOrderStatisticCommand.orderDuplicate(year, month, day, cityNames);
    }


    /**
     * 按工单全部类型统计.
     */
    /**
     * 按工单全部类型统计.
     */
    @McpTool(
            name = "getWorkOrderTypeStatistic",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定日期的工单二级分类和三级分类统计, 同时返回指定月份整月工单数量.
                    
                    统计口径：
                    - 按工单创建时间统计.
                    - 不限制工单状态.
                    - 包含进行中、已完成等全部工单.
                    - 按一级、二级、三级工单类型进行详细分类统计.
                    - 统计值为工单数量.
                    
                    统计维度：
                    - 一级工单类型 order_type
                    - 二级工单类型 second_type
                    - 三级工单类型 third_type
                    - 城市
                    
                    返回内容：
                    - 一级工单类型
                    - 二级工单类型
                    - 三级工单类型
                    - 城市ID
                    - 城市名称
                    - 当日工单数量
                    - 当月工单数量
                    
                    时间规则：
                    - day 必须指定具体日期, 取值范围：1~31.
                    - 当日工单数量表示指定日期创建的工单数量.
                    - 当月工单数量表示指定月份整月创建的工单数量.
                    - 工具会自动同时查询指定日期和指定月份整月数据.
                    - 调用工具时不需要额外指定月度统计参数.
                    
                    城市参数：
                    - 不指定城市时, 查询全部城市.
                    - 指定一个城市时, 查询该城市.
                    - 指定多个城市时, 查询指定城市.
                    
                    注意：
                    - 本工具统计的是工单数量, 不是用户数量.
                    - 不根据工单状态进行筛选.
                    - 一级、二级、三级工单类型共同构成详细工单分类维度.
                    - 如果用户只提供年份和月份而没有提供具体日期, 应要求用户补充具体查询日期.
                    """
    )
    public WorkOrderTypeStatisticVo getWorkOrderTypeStatistic(
            @McpToolParam(description = "统计年份, 例如：2026") int year,
            @McpToolParam(description = "统计月份, 取值范围：1~12, 例如：9") int month,
            @McpToolParam(description = "统计日期, 必须指定具体日期, 取值范围：1~31, 例如：15. 不允许传0. 工具会自动返回指定日期和指定月份整月统计") int day,
            @McpToolParam(description = "城市名称列表, 例如：[北京市, 上海市, 广州市]. 不指定城市表示查询全部城市") List<String> cityNames) {
        return workOrderStatisticCommand.orderTypeStatistic(year, month, day, cityNames);
    }

}
