package cn.net.gwbn.ai.mcp.sales.api.income;

import cn.net.gwbn.ai.mcp.sales.api.SummaryVieObject;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lixiaofeng
 * @date 6/28/26 AM10:43
 **/
public class CityIncomeSummaryVo extends SummaryVieObject {

    /**
     * 年
     */
    private int year;

    /**
     * 月
     */
    private int month;

    /**
     * 日
     */
    private int day;

    /**
     * 当月收入汇总（所有城市）
     */
    private BigDecimal totalMonthIncome;


    private String displayTotalMonthIncome;

    /**
     * 当日收入汇总（所有城市）
     */
    private BigDecimal totalDayIncome;


    private String displayTotalDayIncome;

    /**
     * 单位
     */
    private String unit;

    /**
     * 城市收入列表
     */
    private List<CityIncomeVo> incomeVos;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<CityIncomeVo> getIncomeVos() {
        return incomeVos;
    }

    public void setIncomeVos(List<CityIncomeVo> incomeVos) {
        this.incomeVos = incomeVos;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getTotalMonthIncome() {
        return totalMonthIncome;
    }

    public void setTotalMonthIncome(BigDecimal totalMonthIncome) {
        this.totalMonthIncome = totalMonthIncome;
    }

    public BigDecimal getTotalDayIncome() {
        return totalDayIncome;
    }

    public void setTotalDayIncome(BigDecimal totalDayIncome) {
        this.totalDayIncome = totalDayIncome;
    }

    public String getDisplayTotalMonthIncome() {
        return displayTotalMonthIncome;
    }

    public void setDisplayTotalMonthIncome(String displayTotalMonthIncome) {
        this.displayTotalMonthIncome = displayTotalMonthIncome;
    }

    public String getDisplayTotalDayIncome() {
        return displayTotalDayIncome;
    }

    public void setDisplayTotalDayIncome(String displayTotalDayIncome) {
        this.displayTotalDayIncome = displayTotalDayIncome;
    }
}
