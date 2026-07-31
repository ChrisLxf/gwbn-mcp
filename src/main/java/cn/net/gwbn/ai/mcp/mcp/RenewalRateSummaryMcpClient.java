package cn.net.gwbn.ai.mcp.mcp;

import cn.net.gwbn.ai.mcp.sales.api.user.RenewalRateSummaryVo;
import cn.net.gwbn.ai.mcp.sales.command.RenewalCommand;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

import java.util.List;

/**
 * @author lixiaofeng
 * @date 7/28/26 PM3:38
 **/
public class RenewalRateSummaryMcpClient {


    private final RenewalCommand renewalCommand;


    public RenewalRateSummaryMcpClient(RenewalCommand renewalCommand) {
        this.renewalCommand = renewalCommand;
    }


    /**
     * 查询用户续费率
     */
    @McpTool(name = "getRenewalRateSummary",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true),
            description = """
                    查询指定月份用户续费率统计。
                    
                    该指标用于分析宽带用户到期后的续费情况。
                    
                    核心概念：
                    - t 表示用户距离到期的月份周期，不表示日期。
                    - t=0 表示查询当月到期用户的续费率（T+0）。
                    - t=1 表示查询未来1个月到期用户的续费率（T+1）。
                    - t=2 表示查询未来2个月到期用户的续费率（T+2）。
                    - t=3 表示查询未来3个月到期用户的续费率（T+3）。
                    - t=n 表示查询未来n个月到期用户的续费率（T+n）。

                    计算方式：
                    续费率 = 已续费用户数 / 应续费用户数 × 100%
                    
                    返回内容：
                    - 统计年份
                    - 统计月份
                    - 到期周期T+n
                    - 城市续费统计列表
                        - 城市ID
                        - 城市名称
                        - 应续费用户数量
                        - 已续费用户数量
                        - 续费率
                    
                    调用场景：
                    - 查询未来一个月到期用户续费率
                    - 查询未来多个周期用户续费情况
                    - 分析城市续费能力
                    - 宽带用户流失风险分析
                    
                    城市参数说明：
                    - cityNames为空表示查询全部城市。
                    - cityNames传入城市名称列表表示查询指定城市。
                    """
    )
    public RenewalRateSummaryVo getRenewalRateSummary(
            @McpToolParam(description = "统计年份，例如：2026") int year,
            @McpToolParam(description = "统计月份，取值范围1~12，例如：7") int month,
            @McpToolParam(description = """
                    到期周期T+n。
                    
                    核心概念：
                    - t 表示用户距离到期的月份周期，不表示日期。
                    - t=0 表示查询当月到期用户的续费率（T+0）。
                    - t=1 表示查询未来1个月到期用户的续费率（T+1）。
                    - t=2 表示查询未来2个月到期用户的续费率（T+2）。
                    - t=3 表示查询未来3个月到期用户的续费率（T+3）。
                    - t=n 表示查询未来n个月到期用户的续费率（T+n）。
                    
                    例如：
                    查询2026年7月份未来1个月到期用户续费率，
                    t传1。
                    
                    取值范围：1~240，例如：1
                    """
            ) int t,
            @McpToolParam(description = """
                    城市名称列表。
                    
                    示例：
                    ["北京市","上海市","青岛市"]
                    
                    为空时查询全部城市。
                    """
            ) List<String> cityNames) {


        return renewalCommand.summary(year, month, t, cityNames);
    }

}