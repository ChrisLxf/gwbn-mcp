package cn.net.gwbn.ai.mcp.mcp;

import cn.net.gwbn.ai.mcp.sales.api.workorder.WorTaskAverageDurationVo;
import cn.net.gwbn.ai.mcp.sales.api.workorder.WorkTaskCountStatisticVo;
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
public class WorkTaskMcpClient {

    /**
     * 非单工单类型.
     */
    private static final String MASS_ORDER_TYPE = "非单";

    /**
     * 非单工单一线施工任务.
     */
    private static final List<String> MASS_ORDER_TASK_NAMES = Arrays.asList("部门施工", "上门施工");


    private static final List<String> CLAIM_ORDER_TASK_NAMES = Arrays.asList("下派一线");

    private final WorTaskAverageDurationCommand command;

    public WorkTaskMcpClient(WorTaskAverageDurationCommand command) {
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
    public WorTaskAverageDurationVo queryMassAverageDuration(
            @McpToolParam(description = "统计年份, 例如：2026") int year,
            @McpToolParam(description = "统计月份, 取值范围：1~12, 例如：9") int month,
            @McpToolParam(description = "统计日期, 取值范围：1~31。day > 0 查询指定日期, 例如：15") int day,
            @McpToolParam(description = "城市名称列表。为空表示查询全部城市, 例如：[北京市, 上海市, 广州市]") List<String> cityNames) {

        return command.taskMassAverageDurationStatistic(year, month, day, cityNames, MASS_ORDER_TASK_NAMES);
    }

    /**
     * 查询一线接单平均完成时长.
     *
     * @param year      统计年份
     * @param month     统计月份
     * @param day       统计日期
     * @param orderType 工单类型, 可为空
     * @param cityNames 城市名称列表
     * @return 一线接单平均完成时长
     */
    /**
     * 查询一线接单平均完成时长.
     *
     * @param year      统计年份
     * @param month     统计月份
     * @param day       统计日期
     * @param cityNames 城市名称列表
     * @return 一线接单平均完成时长
     */
    @McpTool(
            name = "getWorkOrderClaimAverageDuration",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询工单一线接单任务的平均完成时长。
                    
                    【业务定义】
                    一线接单是工单处理流程中的接单环节。
                    本工具用于统计已经完成的"一线接单"任务的平均完成时长,
                    用于分析一线接单环节的处理效率。
                    
                    【统计范围】
                    - 任务名称固定为"一线接单"。
                    - 只统计已经完成的一线接单任务。
                    - task_state = COMPLETED。
                    - 按工单下派（创建）时间进行统计。
                    - 不限制工单类型。
                    - 查询结果包含全部工单类型。
                    
                    
                    【统计维度】
                    - 工单类型
                    - 城市
                    - 任务名称
                    
                    【返回数据】
                    返回一线接单平均完成时长统计结果。
                    
                    统计结果包含：
                    - 工单类型
                    - 城市ID
                    - 城市名称
                    - 任务名称
                    - 当日平均完成时长
                    - 当月平均完成时长
                    
                    工单类型仅用于返回结果分类,
                    不能作为查询条件。
                    
                    【平均完成时长】
                    - duration 表示一线接单任务的完成时长。
                    - 数据库 duration 单位为毫秒。
                    - 返回结果中的平均完成时长单位为分钟。
                    - 平均完成时长保留2位小数。
                    
                    【城市查询】
                    cityNames 是本工具支持的业务筛选条件。
                    
                    - 用户未指定城市：
                      cityNames 传空值, 查询全部城市。
                    
                    - 用户指定一个城市：
                      cityNames 传包含该城市名称的列表。
                    
                    - 用户指定多个城市：
                      cityNames 传包含这些城市名称的列表。
                    
                    【日期查询】
                    - day > 0：
                      查询指定日期下派的一线接单任务平均完成时长,
                      同时返回该月份的一线接单平均完成时长。
                    
                    
                    
                    【适用场景】
                    - 查询今天一线接单平均需要多长时间
                    - 查询本月一线接单平均完成时长
                    - 查询今天各类工单一线接单平均完成时长
                    - 查询各城市一线接单平均完成时长
                    - 查询北京今天一线接单平均完成时长
                    - 查询本月各城市一线接单平均完成时长
                    
                    【参数使用规则】
                    - year 使用用户指定的统计年份。
                    - month 使用用户指定的统计月份。
                    - 用户指定具体日期时, day 传该日期。
                    - 用户未指定城市时, cityNames 传空值。
                    - 用户指定城市时, cityNames 传对应城市名称列表。
                    - 不要根据用户提到的工单类型进行过滤。
                    
                    【重要边界】
                    - 本工具只统计"一线接单"任务。
                    - 只统计已经完成的一线接单任务。
                    - 不统计"部门施工"、"上门施工"等其他任务。
                    - 工单类型不能作为查询条件。
                    - 工单类型只作为返回结果统计维度。
                    - 用户提到具体工单类型时, 仍然查询全部工单类型。
                    - 不应使用本工具查询其他任务环节的平均完成时长。
                    """
    )
    public WorTaskAverageDurationVo getWorkOrderClaimAverageDuration(
            @McpToolParam(description = "统计年份, 例如：2026") int year,
            @McpToolParam(description = "统计月份, 取值范围：1~12, 例如：9") int month,
            @McpToolParam(description = "统计日期, 取值范围：1~31。day > 0 查询指定日期, 例如：15") int day,
            @McpToolParam(description = "城市名称列表。为空表示查询全部城市, 例如：[北京市, 上海市, 广州市]") List<String> cityNames) {
        return command.taskAverageDurationStatistic(year, month, day, cityNames, CLAIM_ORDER_TASK_NAMES);
    }


    @McpTool(
            name = "getWorkOrderTaskNotAssignee",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询工单任务无人接单统计。
                    
                    【业务定义】
                    无人接单是指工单已经进入"下派一线"任务环节,
                    但当前任务仍处于运行状态且没有分配接单人。
                    
                    固定统计条件：
                    - task_name = "下派一线"
                    - task_state = "RUNNING"
                    - assignee IS NULL
                    
                    因此, 本工具统计的是：
                    已经下派到一线, 当前仍未完成, 且没有一线接单人的任务数量。
                    
                    【查询条件】
                    本工具支持以下查询条件：
                    - year：统计年份
                    - month：统计月份
                    - day：统计日期
                    - cityNames：城市名称列表
                    
                    本工具不支持工单类型筛选。
                    
                    【工单类型规则】
                    orderType 是返回结果中的统计维度,
                    不是查询参数。
                    
                    用户无论是否提到具体工单类型,
                    查询时都必须统计全部工单类型。
                    
                    例如：
                    - 用户说"查询今天无人接单数量"
                      -> 查询全部工单类型。
                    
                    - 用户说"查询今天各类工单无人接单数量"
                      -> 查询全部工单类型,
                      -> 通过返回结果中的 orderType 区分工单类型。
                    
                    - 用户说"查询今天故障工单无人接单数量"
                      -> 本工具没有工单类型筛选参数,
                      -> 仍然查询全部工单类型,
                      -> 不要构造 orderType 参数,
                      -> 返回结果中包含各工单类型的数据。
                    
                    【城市查询规则】
                    cityNames 是唯一的业务筛选维度。
                    
                    - 用户未指定城市：
                      cityNames 传空值, 查询全部城市。
                    
                    - 用户指定一个城市：
                      cityNames 传包含该城市名称的列表。
                    
                    - 用户指定多个城市：
                      cityNames 传包含这些城市名称的列表。
                    
                    【日期规则】
                    - day > 0：
                      查询指定日期的无人接单任务数量,
                      同时返回该月份累计无人接单任务数量。
                    
                    
                    【返回结果】
                    返回 WorkTaskCountStatisticVo。
                    
                    顶层字段：
                    - year：统计年份
                    - month：统计月份
                    - day：统计日期
                    - dimension：统计维度
                    - workTaskCountStatisticItemVoList：统计明细列表
                    
                    明细字段：
                    - orderType：工单类型
                    - cityId：城市ID
                    - cityName：城市名称
                    - taskName：任务名称
                    - dayCount：指定日期的无人接单数量
                    - monthCount：当月无人接单数量
                    
                    每一条明细数据按照以下维度区分：
                    - 工单类型
                    - 城市
                    - 任务名称
                    
                    因此返回结果可能包含多个工单类型,
                    多个城市以及对应的任务统计数据。
                    
                    【参数调用规则】
                    - year 使用用户指定的年份。
                    - month 使用用户指定的月份。
                    - 用户指定具体日期时, day 传该日期。
                    - 用户未指定城市时, cityNames 传空值。
                    - 用户指定城市时, cityNames 传对应城市名称列表。
                    - 不要传入 orderType。
                    - 不要根据用户提到的工单类型进行过滤。
                    
                    【重要边界】
                    - 只统计 task_name = "下派一线" 的任务。
                    - 只统计 task_state = "RUNNING" 的任务。
                    - 只统计 assignee IS NULL 的任务。
                    - 不统计已经完成的任务。
                    - 不统计已经分配接单人的任务。
                    - 不统计其他任务环节。
                    - 工单类型只作为返回结果维度, 不能作为查询条件。
                    - 不应使用本工具查询平均完成时长。
                    """
    )
    public WorkTaskCountStatisticVo getWorkOrderTaskNotAssignee(
            @McpToolParam(description = "统计年份, 例如：2026") int year,
            @McpToolParam(description = "统计月份, 取值范围：1~12, 例如：9") int month,
            @McpToolParam(description = "统计日期, 取值范围：1~31。day > 0 查询指定日期，例如：15") int day,
            @McpToolParam(description = "城市名称列表。为空表示查询全部城市, 例如：[北京市, 上海市, 广州市]") List<String> cityNames) {
        return command.taskNotAssignee(year, month, day, cityNames);
    }


    /**
     * 一线人员工作量统计
     *
     * @param year
     * @param month
     * @param day
     * @param cityNames
     * @return
     */
    @McpTool(
            name = "getFrontLineTaskCount",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询一线人员工作量统计。
                    
                    【业务定义】
                    一线人员工作量是指工单进入"下派一线"任务环节后产生的一线任务数量。
                    
                    本工具固定统计：
                    - task_name = "下派一线"
                    
                    因此, 本工具统计的是"下派一线"任务数量,
                    用于反映一线人员任务工作量。
                    
                    注意：
                    - 本指标统计的是任务数量, 不是工单数量。
                    - 本指标统计的是工作量, 不是一线接单平均完成时长。
                    - 本指标也不是无人接单任务数量。
                    
                    【统计范围】
                    - 任务名称固定为"下派一线"
                    - 按任务创建时间进行统计
                    - 支持按城市查询
                    - 工单类型不作为查询条件
                    - 返回结果可以按照工单类型进行分类统计
                    
                    【统计维度】
                    - 工单类型
                    - 城市
                    - 任务名称
                    
                    【返回数据】
                    返回一线任务工作量统计结果。
                    
                    统计结果包含：
                    - 工单类型
                    - 城市ID
                    - 城市名称
                    - 任务名称
                    - 当日任务数量
                    - 当月任务数量
                    
                    【数量含义】
                    dayCount：
                    - 用户指定 day > 0 时, 表示指定日期创建的"下派一线"任务数量。
                    
                    monthCount：
                    - 表示指定月份创建的"下派一线"任务数量。
                    
                    当 day = 0 时：
                    - 查询整月"下派一线"任务数量。
                    
                    【城市查询】
                    cityNames 是本工具支持的业务筛选条件。
                    
                    - 用户未指定城市：
                      cityNames 传空值, 查询全部城市。
                    
                    - 用户指定一个城市：
                      cityNames 传包含该城市名称的列表。
                    
                    - 用户指定多个城市：
                      cityNames 传包含这些城市名称的列表。
                    
                    【日期查询】
                    - day > 0：
                      查询指定日期的一线任务工作量,
                      同时返回该月份的一线任务累计工作量。
                    
                    - day = 0：
                      查询整月的一线任务工作量。
                    
                    【工单类型规则】
                    本工具不提供工单类型查询参数。
                    
                    用户无论是否提到具体工单类型,
                    调用本工具时都不要构造 orderType 参数。
                    
                    工单类型只作为返回结果中的统计维度。
                    
                    例如：
                    - 用户说"查询今天一线人员工作量"
                      -> 查询全部工单类型。
                    
                    - 用户说"查询今天各类工单的一线工作量"
                      -> 查询全部工单类型,
                      -> 根据返回结果中的 orderType 区分工单类型。
                    
                    【适用场景】
                    当用户询问以下问题时, 可以使用本工具：
                    - 一线人员今天工作量
                    - 一线人员本月工作量
                    - 今天下派了一线多少任务
                    - 本月下派一线任务多少
                    - 各城市一线工作量
                    - 北京一线人员工作量
                    - 各类工单一线工作量
                    - 一线任务数量
                    - 一线工作量统计
                    - 一线人员任务量
                    
                    【与其他指标的区别】
                    
                    1. 一线工作量
                       使用本工具。
                       统计 task_name = "下派一线" 的任务数量。
                    
                    2. 一线接单平均完成时长
                       使用 getWorkOrderClaimAverageDuration。
                       统计已完成"下派一线"任务的平均完成时长。
                    
                    3. 一线无人接单任务
                       使用 getWorkOrderTaskNotAssignee。
                       统计 task_name = "下派一线",
                       task_state = "RUNNING",
                       assignee IS NULL 的任务数量。
                    
                    三者不能互相替代。
                    
                    【重要边界】
                    - 只统计 task_name = "下派一线"。
                    - 不统计"部门施工"任务。
                    - 不统计"上门施工"任务。
                    - 不统计其他任务环节。
                    - 不把任务数量解释为接单人数。
                    - 不把任务数量解释为无人接单数量。
                    - 不把任务数量解释为工单数量。
                    - 不使用工单类型作为查询条件。
                    - 不应使用本工具查询平均完成时长。
                    
                    【参数调用规则】
                    - year 使用用户指定的统计年份。
                    - month 使用用户指定的统计月份。
                    - 用户指定具体日期时, day 传该日期。
                    - 用户未指定具体日期时, day=0。
                    - 用户未指定城市时, cityNames 传空值。
                    - 用户指定城市时, cityNames 传对应城市名称列表。
                    - 不要传入 orderType。
                    
                    【数据真实性】
                    - 只根据实际查询结果返回数据。
                    - 不自行推算任务数量。
                    - 不将任务数量转换成用户数量。
                    - 不将任务数量转换成接单人数。
                    - 不自行计算不存在的同比、环比或增长率。
                    - 查询无数据时返回暂无数据。
                    """
    )
    public WorkTaskCountStatisticVo getFrontLineTaskCount(
            @McpToolParam(description = "统计年份, 例如：2026") int year,
            @McpToolParam(description = "统计月份, 取值范围：1~12, 例如：9") int month,
            @McpToolParam(description = "统计日期, 取值范围：1~31。day > 0 查询指定日期, day = 0 查询整月, 例如：15") int day,
            @McpToolParam(description = "城市名称列表。为空表示查询全部城市, 例如：[北京市, 上海市, 广州市]") List<String> cityNames) {
        return command.taskCount(year, month, day, cityNames, List.of("下派一线"), false);
    }


    /**
     * 一线人员完成工作量
     */
    @McpTool(
            name = "getFrontLineCompletedTaskCount",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询一线人员完成工作量统计。
                    
                    【业务定义】
                    一线人员完成工作量是指"上门施工"任务中已经完成的任务数量。
                    
                    本工具固定统计：
                    - task_name = "上门施工"
                    - task_state = "COMPLETED"
                    
                    因此, 本工具统计的是已完成的"上门施工"任务数量,
                    用于反映一线人员上门施工环节的完成工作量。
                    
                    注意：
                    - 本指标统计的是任务数量, 不是一线人员人数。
                    - 本指标统计的是已完成任务数量, 不是当前正在处理任务数量。
                    - 本指标不是一线无人接单数量。
                    - 本指标不是任务平均完成时长。
                    - 本指标不是工单数量。
                    
                    【统计范围】
                    - 任务名称固定为"上门施工"
                    - 只统计已经完成的任务
                    - task_state = COMPLETED
                    - 按任务创建时间进行统计
                    - 支持按城市查询
                    - 不提供工单类型筛选参数
                    
                    【统计维度】
                    - 工单类型
                    - 城市
                    - 任务名称
                    
                    【返回数据】
                    返回 WorkTaskCountStatisticVo。
                    
                    统计结果包含：
                    - 工单类型
                    - 城市ID
                    - 城市名称
                    - 任务名称
                    - 当日完成任务数量
                    - 当月完成任务数量
                    
                    【数量含义】
                    dayCount：
                    - 当 day > 0 时, 表示指定日期创建的"上门施工"任务中,
                      已完成任务的数量。
                    
                    monthCount：
                    - 表示指定月份创建的"上门施工"任务中,
                      已完成任务的数量。
                    
                    当 day = 0 时：
                    - 查询整月已完成的"上门施工"任务数量。
                    
                    【城市查询】
                    cityNames 是本工具支持的业务筛选条件。
                    
                    - 用户未指定城市：
                      cityNames 传空值, 查询全部城市。
                    
                    - 用户指定一个城市：
                      cityNames 传包含该城市名称的列表。
                    
                    - 用户指定多个城市：
                      cityNames 传包含这些城市名称的列表。
                    
                    【日期查询】
                    - day > 0：
                      查询指定日期的已完成"上门施工"任务数量,
                      同时返回该月份累计已完成"上门施工"任务数量。
                    
                    - day = 0：
                      查询整月已完成"上门施工"任务数量。
                    
                    注意：
                    日期统计按照任务创建时间进行,
                    不是按照任务完成时间进行。
                    
                    【工单类型规则】
                    本工具不提供工单类型查询参数。
                    
                    用户无论是否提到具体工单类型,
                    调用本工具时都不要构造 orderType 参数。
                    
                    工单类型只作为返回结果中的统计维度。
                    
                    例如：
                    - 用户说"今天一线完成了多少工作量"
                      -> 查询全部工单类型。
                    
                    - 用户说"今天各类工单一线完成工作量"
                      -> 查询全部工单类型,
                      -> 根据返回结果中的 orderType 区分工单类型。
                    
                    - 用户说"今天故障工单一线完成了多少工作量"
                      -> 本工具没有工单类型筛选参数,
                      -> 仍然查询全部工单类型,
                      -> 不构造 orderType 参数。
                    
                    【适用场景】
                    当用户询问以下问题时, 可以使用本工具：
                    - 一线人员今天完成多少工作量
                    - 一线人员本月完成多少工作量
                    - 今天完成了多少上门施工
                    - 本月完成了多少上门施工任务
                    - 各城市一线完成工作量
                    - 北京一线完成工作量
                    - 一线人员上门施工完成量
                    - 一线施工完成任务数量
                    - 一线人员完成任务数量
                    
                    【与其他指标的区别】
                    
                    1. 一线人员完成工作量
                       使用本工具。
                       统计 task_name = "上门施工" 且
                       task_state = "COMPLETED" 的任务数量。
                    
                    2. 一线人员工作量
                       如果统计的是"下派一线"任务数量,
                       使用 getFrontLineTaskCount。
                    
                    3. 一线接单平均完成时长
                       使用 getWorkOrderClaimAverageDuration。
                       统计已完成"下派一线"任务的平均完成时长。
                    
                    4. 一线无人接单任务
                       使用 getWorkOrderTaskNotAssignee。
                       统计 task_name = "下派一线",
                       task_state = "RUNNING",
                       assignee IS NULL 的任务数量。
                    
                    以上指标不能互相替代。
                    
                    【重要边界】
                    - 只统计 task_name = "上门施工"。
                    - 只统计 task_state = "COMPLETED"。
                    - 不统计"下派一线"任务。
                    - 不统计"部门施工"任务。
                    - 不统计其他任务环节。
                    - 不把任务数量解释为人员数量。
                    - 不把任务数量解释为工单数量。
                    - 不把任务数量解释为无人接单数量。
                    - 不应使用本工具查询平均完成时长。
                    
                    【参数调用规则】
                    - year 使用用户指定的统计年份。
                    - month 使用用户指定的统计月份。
                    - 用户指定具体日期时, day 传该日期。
                    - 用户未指定具体日期时, day=0。
                    - 用户未指定城市时, cityNames 传空值。
                    - 用户指定城市时, cityNames 传对应城市名称列表。
                    - 不要传入 orderType。
                    
                    【数据真实性】
                    - 只根据实际查询结果返回数据。
                    - 不自行推算完成任务数量。
                    - 不将任务数量转换成用户数量。
                    - 不将任务数量转换成人员数量。
                    - 不自行计算不存在的同比、环比或增长率。
                    - 查询无数据时返回暂无数据。
                    """
    )
    public WorkTaskCountStatisticVo getFrontLineCompletedTaskCount(
            @McpToolParam(description = "统计年份, 例如：2026") int year,
            @McpToolParam(description = "统计月份, 取值范围：1~12, 例如：9") int month,
            @McpToolParam(description = "统计日期, 取值范围：1~31。day > 0 查询指定日期, day = 0 查询整月, 例如：15") int day,
            @McpToolParam(description = "城市名称列表。为空表示查询全部城市, 例如：[北京市, 上海市, 广州市]") List<String> cityNames) {
        return command.taskCount(year, month, day, cityNames, List.of("上门施工"), true);
    }


    @McpTool(
            name = "getWorkOrderResponseAverageDuration",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询响应工单平均完成时长.
                    
                    用于统计指定年份、月份、日期和城市范围内, 响应工单任务的平均完成时长.
                    
                    统计规则:
                    1. 固定任务类型为"响应工单".
                    2. 仅统计已完成的"响应工单"任务.
                    3. 按任务创建年份、月份统计; day > 0 时进一步限定任务创建日期.
                    4. cityNames 为空时统计全部城市; 指定城市时仅统计指定城市.
                    5. 返回结果包含按城市、工单类型、任务类型等维度统计的平均完成时长.
                    6. 平均完成时长仅针对已完成任务计算.
                    
                    适用问题:
                    - 响应工单平均完成时长是多少?
                    - 某城市响应工单平均完成需要多长时间?
                    - 查询某月/某日响应工单平均完成时长.
                    """)
    public WorTaskAverageDurationVo getWorkOrderResponseAverageDuration(
            @McpToolParam(description = "统计年份, 例如：2026") int year,
            @McpToolParam(description = "统计月份, 取值范围：1~12, 例如：9") int month,
            @McpToolParam(description = "统计日期, 取值范围：1~31。day > 0 查询指定日期, day = 0 查询整月, 例如：15") int day,
            @McpToolParam(description = "城市名称列表。为空表示查询全部城市, 例如：[北京市, 上海市, 广州市]") List<String> cityNames) {
        return command.taskAverageDurationStatistic(year, month, day, cityNames, List.of("响应工单"));
    }


    /**
     * 一线施工平均时长
     *
     * @param year
     * @param month
     * @param day
     * @param cityNames
     * @return
     */
    @McpTool(
            name = "getWorkOrderWorkAverageDuration",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询一线施工平均完成时长.
                    
                    用于统计指定年份、月份、日期和城市范围内, 一线施工任务的平均完成时长.
                    
                    统计规则:
                    1. 固定统计任务类型为"响应工单".
                    2. 仅统计已完成的任务.
                    3. 按任务创建年份、月份进行统计.
                    4. 当 day > 0 时, 仅统计指定日期创建的任务; 当 day = 0 时, 统计整月数据.
                    5. cityNames 为空时统计全部城市; 指定城市时仅统计指定城市.
                    6. 平均完成时长仅针对已完成任务计算.
                    7. 返回按城市、工单类型、任务类型等维度统计的平均完成时长.
                    
                    适用问题:
                    - 一线施工平均完成时长是多少?
                    - 某城市一线施工平均完成需要多长时间?
                    - 查询某月或某日的一线施工平均完成时长.
                    """
    )
    public WorTaskAverageDurationVo getWorkOrderWorkAverageDuration(
            @McpToolParam(description = "统计年份, 例如：2026") int year,
            @McpToolParam(description = "统计月份, 取值范围：1~12, 例如：9") int month,
            @McpToolParam(description = "统计日期, 取值范围：1~31。day > 0 查询指定日期, day = 0 查询整月, 例如：15") int day,
            @McpToolParam(description = "城市名称列表。为空表示查询全部城市, 例如：[北京市, 上海市, 广州市]") List<String> cityNames) {
        return command.taskAverageDurationStatistic(year, month, day, cityNames, List.of("上门施工"));
    }

}