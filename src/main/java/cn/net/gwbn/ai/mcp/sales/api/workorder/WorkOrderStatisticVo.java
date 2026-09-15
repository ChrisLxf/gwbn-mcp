package cn.net.gwbn.ai.mcp.sales.api.workorder;

import cn.net.gwbn.ai.mcp.sales.api.SummaryVieObject;

import java.util.List;

/**
 * 工单统计展示模型
 *
 * @author lixiaofeng
 * @date 9/14/26 PM1:28
 **/
public class WorkOrderStatisticVo  extends SummaryVieObject {

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
     * 统计维度
     */
    private String dimension;

    /**
     * 统计指标
     */
    private String metric;

    /**
     * 统计结果
     */
    private List<WorkOrderStatisticItemVo> items;


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

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public List<WorkOrderStatisticItemVo> getItems() {
        return items;
    }

    public void setItems(List<WorkOrderStatisticItemVo> items) {
        this.items = items;
    }
}