package cn.net.gwbn.ai.mcp.sales.api.workorder;

import cn.net.gwbn.ai.mcp.sales.api.SummaryVieObject;

import java.util.List;

/**
 * 批量工单完成率统计.
 *
 * @author lixiaofeng
 * @date 9/15/26
 **/
public class MassOrderCompletionRateVo extends SummaryVieObject {

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
     * 统计维度.
     */
    private String dimension;

    /**
     * 完成率明细.
     */
    private List<MassOrderCompletionRateItemVo> items;

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

    public List<MassOrderCompletionRateItemVo> getItems() {
        return items;
    }

    public void setItems(List<MassOrderCompletionRateItemVo> items) {
        this.items = items;
    }
}