package cn.net.gwbn.ai.mcp.sales.api.workorder;

import java.math.BigDecimal;

/**
 * 批量工单完成率明细.
 *
 * @author lixiaofeng
 * @date 9/15/26
 **/
public class MassOrderCompletionRateItemVo {

    /**
     * 故障类型.
     */
    private String faultType;

    /**
     * 城市ID.
     */
    private String cityId;

    /**
     * 城市名称.
     */
    private String cityName;

    /**
     * 当日全部批量工单数量.
     */
    private int dayTotalCount;

    /**
     * 当日已完成批量工单数量.
     */
    private int dayCompletedCount;

    /**
     * 当日完成率.
     */
    private BigDecimal dayCompletionRate;

    /**
     * 当月全部批量工单数量.
     */
    private int monthTotalCount;

    /**
     * 当月已完成批量工单数量.
     */
    private int monthCompletedCount;

    /**
     * 当月完成率.
     */
    private BigDecimal monthCompletionRate;

    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
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