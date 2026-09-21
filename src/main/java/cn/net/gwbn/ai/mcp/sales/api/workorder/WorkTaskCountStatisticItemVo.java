package cn.net.gwbn.ai.mcp.sales.api.workorder;

/**
 * @author lixiaofeng
 * @date 9/17/26 PM1:53
 **/
public class WorkTaskCountStatisticItemVo {


    /**
     * 工单类型.
     */
    private String orderType;

    /**
     * 城市ID.
     */
    private String cityId;

    /**
     * 城市名称.
     */
    private String cityName;

    /**
     * 任务名称.
     */
    private String taskName;


    /**
     * 当日数量
     */
    private int dayCount;

    /**
     * 当月数量
     */
    private int monthCount;

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public int getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(int monthCount) {
        this.monthCount = monthCount;
    }
}
