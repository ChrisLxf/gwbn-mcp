package cn.net.gwbn.ai.mcp.sales.command.workorder;

/**
 * @author lixiaofeng
 * @date 9/14/26 PM1:32
 **/
public enum WorkOrderMetric {

    /**
     * 工单数量
     */
    COUNT,

    /**
     * 处理时长总和
     */
    DURATION_SUM;

    public static WorkOrderMetric from(String value) {

        for (WorkOrderMetric metric : values()) {
            if (metric.name().equalsIgnoreCase(value)) {
                return metric;
            }
        }

        throw new IllegalArgumentException("Unsupported work order metric: " + value);
    }
}