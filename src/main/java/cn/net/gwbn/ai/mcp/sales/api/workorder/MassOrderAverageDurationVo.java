package cn.net.gwbn.ai.mcp.sales.api.workorder;

import cn.net.gwbn.ai.mcp.sales.api.SummaryVieObject;

import java.math.BigDecimal;
import java.util.List;

/**
 * 非单户故障平均完成时长统计.
 *
 * @author lixiaofeng
 * @date 9/14/26
 **/
public class MassOrderAverageDurationVo extends SummaryVieObject {

    private int year;
    private int month;
    private int day;
    private String dimension;
    private BigDecimal dayAverageDuration;
    private BigDecimal monthAverageDuration;
    private List<MassOrderAverageDurationItemVo> items;

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

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
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

    public List<MassOrderAverageDurationItemVo> getItems() {
        return items;
    }

    public void setItems(List<MassOrderAverageDurationItemVo> items) {
        this.items = items;
    }
}