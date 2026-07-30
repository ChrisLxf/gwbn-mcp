package cn.net.gwbn.ai.mcp.sales.api.income;

import java.math.BigDecimal;

/**
 * 城市收入
 *
 * @author lixiaofeng
 * @date 6/28/26 AM10:44
 **/
public class CityIncomeVo {

    /**
     * 城市ID
     */
    private String cityId;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 当月收入
     */
    private BigDecimal monthIncome;

    private String displayMonthIncome;

    /**
     * 当日收入
     */
    private BigDecimal dayIncome;


    private String displayDayIncome;


    private String unit;


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

    public BigDecimal getMonthIncome() {
        return monthIncome;
    }

    public void setMonthIncome(BigDecimal monthIncome) {
        this.monthIncome = monthIncome;
    }

    public BigDecimal getDayIncome() {
        return dayIncome;
    }

    public void setDayIncome(BigDecimal dayIncome) {
        this.dayIncome = dayIncome;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDisplayMonthIncome() {
        return displayMonthIncome;
    }

    public void setDisplayMonthIncome(String displayMonthIncome) {
        this.displayMonthIncome = displayMonthIncome;
    }

    public String getDisplayDayIncome() {
        return displayDayIncome;
    }

    public void setDisplayDayIncome(String displayDayIncome) {
        this.displayDayIncome = displayDayIncome;
    }
}
