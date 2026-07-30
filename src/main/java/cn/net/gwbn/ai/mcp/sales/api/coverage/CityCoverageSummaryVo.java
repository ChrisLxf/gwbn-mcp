package cn.net.gwbn.ai.mcp.sales.api.coverage;

import cn.net.gwbn.ai.mcp.sales.api.SummaryVieObject;

import java.util.List;

/**
 * 城市覆盖数统计
 *
 * @author lixiaofeng
 * @date 7/30/26 PM3:09
 **/
public class CityCoverageSummaryVo extends SummaryVieObject {


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

    private List<CityCoverageItem> cityCoverageItemList;

    private long totalCoverageNumber;

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

    public long getTotalCoverageNumber() {
        return totalCoverageNumber;
    }

    public void setTotalCoverageNumber(long totalCoverageNumber) {
        this.totalCoverageNumber = totalCoverageNumber;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<CityCoverageItem> getCityCoverageItemList() {
        return cityCoverageItemList;
    }

    public void setCityCoverageItemList(List<CityCoverageItem> cityCoverageItemList) {
        this.cityCoverageItemList = cityCoverageItemList;
    }
}
