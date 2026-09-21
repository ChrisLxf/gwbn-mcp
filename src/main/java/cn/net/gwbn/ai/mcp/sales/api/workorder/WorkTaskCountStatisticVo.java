package cn.net.gwbn.ai.mcp.sales.api.workorder;

import cn.net.gwbn.ai.mcp.sales.api.SummaryVieObject;

import java.util.List;

/**
 * @author lixiaofeng
 * @date 9/17/26 PM1:52
 **/
public class WorkTaskCountStatisticVo extends SummaryVieObject {


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
     * 统计维度
     */
    private String dimension;


    private List<WorkTaskCountStatisticItemVo> workTaskCountStatisticItemVoList;

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

    public List<WorkTaskCountStatisticItemVo> getWorkTaskCountStatisticItemVoList() {
        return workTaskCountStatisticItemVoList;
    }

    public void setWorkTaskCountStatisticItemVoList(List<WorkTaskCountStatisticItemVo> workTaskCountStatisticItemVoList) {
        this.workTaskCountStatisticItemVoList = workTaskCountStatisticItemVoList;
    }
}
