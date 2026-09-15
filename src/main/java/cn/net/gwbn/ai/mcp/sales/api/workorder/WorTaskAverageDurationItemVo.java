package cn.net.gwbn.ai.mcp.sales.api.workorder;

import java.math.BigDecimal;

/**
 * @author lixiaofeng
 * @date 9/15/26 AM11:26
 **/
public class WorTaskAverageDurationItemVo {

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
     * 当日平均完成时长, 单位: 分钟.
     */
    private BigDecimal dayAverageDuration;

    /**
     * 当月平均完成时长, 单位: 分钟.
     */
    private BigDecimal monthAverageDuration;

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

    public BigDecimal getDayAverageDuration() {
        return dayAverageDuration;
    }

    public void setDayAverageDuration(BigDecimal dayAverageDuration) {
        this.dayAverageDuration = dayAverageDuration;
    }

    public BigDecimal getMonthAverageDuration() {
        return monthAverageDuration;
    }

    public void setMonthAverageDuration(BigDecimal monthAverageDuration) {
        this.monthAverageDuration = monthAverageDuration;
    }
}