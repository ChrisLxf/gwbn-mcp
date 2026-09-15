package cn.net.gwbn.ai.mcp.sales.api.workorder;

import cn.net.gwbn.ai.mcp.sales.api.SummaryVieObject;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lixiaofeng
 * @date 9/15/26 AM11:27
 **/
public class WorTaskAverageDurationVo extends SummaryVieObject {

    /**
     * 年.
     */
    private int year;

    /**
     * 月.
     */
    private int month;

    /**
     * 日.
     */
    private int day;

    /**
     * 当日平均完成时长, 单位: 分钟.
     */
    private BigDecimal dayAverageDuration;

    /**
     * 当月平均完成时长, 单位: 分钟.
     */
    private BigDecimal monthAverageDuration;

    /**
     * 明细.
     */
    private List<WorTaskAverageDurationItemVo> items;

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

    public List<WorTaskAverageDurationItemVo> getItems() {
        return items;
    }

    public void setItems(List<WorTaskAverageDurationItemVo> items) {
        this.items = items;
    }
}
