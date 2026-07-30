package cn.net.gwbn.ai.mcp.sales.api.business;

import cn.net.gwbn.ai.mcp.sales.api.SummaryVieObject;

import java.util.List;

/**
 * 业务收入
 *
 * @author lixiaofeng
 * @date 6/28/26 PM4:58
 **/
public class BusinessSummaryVo extends SummaryVieObject {

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


    private String unit;

    /**
     * 业务收入列表
     */
    private List<BusinessVo> businessList;


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


    public List<BusinessVo> getBusinessList() {
        return businessList;
    }

    public void setBusinessList(List<BusinessVo> businessList) {
        this.businessList = businessList;
    }


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
