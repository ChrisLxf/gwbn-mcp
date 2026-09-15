package cn.net.gwbn.ai.mcp.sales.api.workorder;

import java.math.BigDecimal;

/**
 * 非单户故障平均完成时长明细.
 *
 * @author lixiaofeng
 * @date 9/14/26
 **/
public class MassOrderAverageDurationItemVo {

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
     * 当日平均完成时长.
     */
    private BigDecimal dayAverageDuration;

    /**
     * 当月平均完成时长.
     */
    private BigDecimal monthAverageDuration;

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