package cn.net.gwbn.ai.mcp.sales.api.workorder;

import cn.net.gwbn.ai.mcp.sales.api.SummaryVieObject;

import java.util.List;

/**
 * @author lixiaofeng
 * @date 9/14/26 PM3:49
 **/
public class MassOrderStatisticVo extends SummaryVieObject {

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
     * 统计结果
     */
    private List<MassOrderStatisticItemVo> items;


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


    public List<MassOrderStatisticItemVo> getItems() {
        return items;
    }

    public void setItems(List<MassOrderStatisticItemVo> items) {
        this.items = items;
    }
}
