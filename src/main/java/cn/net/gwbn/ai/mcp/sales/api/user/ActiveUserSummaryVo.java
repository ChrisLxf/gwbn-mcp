package cn.net.gwbn.ai.mcp.sales.api.user;

import cn.net.gwbn.ai.mcp.sales.api.SummaryVieObject;

import java.util.List;

/**
 * @author lixiaofeng
 * @date 8/19/26 PM1:37
 **/
public class ActiveUserSummaryVo extends SummaryVieObject {

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
     * 总用户数量
     */
    private long totalUserCount;


    private List<UserItemVo> userItems;


    /**
     * 单位
     */
    private String unit;

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

    public long getTotalUserCount() {
        return totalUserCount;
    }

    public void setTotalUserCount(long totalUserCount) {
        this.totalUserCount = totalUserCount;
    }

    public List<UserItemVo> getUserItems() {
        return userItems;
    }

    public void setUserItems(List<UserItemVo> userItems) {
        this.userItems = userItems;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
