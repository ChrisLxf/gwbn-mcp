package cn.net.gwbn.ai.mcp.sales.command.workorder;

/**
 * @author lixiaofeng
 * @date 9/14/26 PM1:31
 **/
public enum WorkOrderDimension {

    /**
     * 工单一级类型
     */
    ORDER_TYPE("order_type", "工单类型"),

    /**
     * 工单二级类型
     */
    SECOND_TYPE("second_type", "二级类型"),

    /**
     * 工单三级类型
     */
    THIRD_TYPE("third_type", "三级类型"),

    /**
     * 工单状态
     */
    ORDER_STATE("order_state", "工单状态"),

    /**
     * 停机状态
     */
    HALT_STATUS("halt_status", "停机状态"),

    /**
     * 用户状态
     */
    USER_STATE("user_state", "用户状态"),

    /**
     * 城市
     */
    CITY("city_name", "城市"),

    /**
     * 区域/营销组
     */
    DISTRICT("distinct_name", "区域"),

    /**
     * 小区
     */
    COMMUNITY("community_name", "小区"),

    /**
     * 处理部门
     */
    HANDLER_DEPARTMENT("handler_dep_name", "处理部门"),

    /**
     * 接单人
     */
    ACCEPTOR("acceptor_name", "接单人"),

    /**
     * 创建人
     */
    CREATE_USER("create_user_name", "创建人"),

    /**
     * 创建日期
     */
    CREATE_DAY("create_day", "日期"),

    /**
     * 创建小时
     */
    CREATE_HOUR("create_hour", "小时");

    private final String column;

    private final String name;

    WorkOrderDimension(String column, String name) {
        this.column = column;
        this.name = name;
    }

    public String getColumn() {
        return column;
    }

    public String getName() {
        return name;
    }

    public static WorkOrderDimension from(String value) {

        for (WorkOrderDimension dimension : values()) {
            if (dimension.name().equalsIgnoreCase(value)) {
                return dimension;
            }
        }

        throw new IllegalArgumentException("Unsupported work order dimension: " + value);
    }
}