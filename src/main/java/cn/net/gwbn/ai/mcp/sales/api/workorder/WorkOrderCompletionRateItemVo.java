package cn.net.gwbn.ai.mcp.sales.api.workorder;

import java.math.BigDecimal;

/**
 * 工单完成率统计明细.
 *
 * @author lixiaofeng
 * @date 9/15/26
 */
public class WorkOrderCompletionRateItemVo {

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
     * 当日全部工单数量.
     */
    private int dayTotalCount;

    /**
     * 当日已完成工单数量.
     */
    private int dayCompletedCount;

    /**
     * 当日完成率, 单位: %.
     */
    private BigDecimal dayCompletionRate;

    /**
     * 当月全部工单数量.
     */
    private int monthTotalCount;

    /**
     * 当月已完成工单数量.
     */
    private int monthCompletedCount;

    /**
     * 当月完成率, 单位: %.
     */
    private BigDecimal monthCompletionRate;

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

    public int getDayTotalCount() {
        return dayTotalCount;
    }

    public void setDayTotalCount(int dayTotalCount) {
        this.dayTotalCount = dayTotalCount;
    }

    public int getDayCompletedCount() {
        return dayCompletedCount;
    }

    public void setDayCompletedCount(int dayCompletedCount) {
        this.dayCompletedCount = dayCompletedCount;
    }

    public BigDecimal getDayCompletionRate() {
        return dayCompletionRate;
    }

    public void setDayCompletionRate(BigDecimal dayCompletionRate) {
        this.dayCompletionRate = dayCompletionRate;
    }

    public int getMonthTotalCount() {
        return monthTotalCount;
    }

    public void setMonthTotalCount(int monthTotalCount) {
        this.monthTotalCount = monthTotalCount;
    }

    public int getMonthCompletedCount() {
        return monthCompletedCount;
    }

    public void setMonthCompletedCount(int monthCompletedCount) {
        this.monthCompletedCount = monthCompletedCount;
    }

    public BigDecimal getMonthCompletionRate() {
        return monthCompletionRate;
    }

    public void setMonthCompletionRate(BigDecimal monthCompletionRate) {
        this.monthCompletionRate = monthCompletionRate;
    }
}