package cn.net.gwbn.ai.mcp.sales.api.workorder;

/**
 * 工单统计明细
 *
 * @author lixiaofeng
 * @date 9/14/26 PM1:29
 **/
public class WorkOrderStatisticItemVo {

    /**
     * 维度值
     */
    private String dimensionValue;

    /**
     * 每日数量
     */
    private int dayCount;

    /**
     * 每月数量
     */
    private int monthCount;

    /**
     * 城市id
     */
    private String cityId;

    /**
     * 城市名称
     */
    private String cityName;

    public String getDimensionValue() {
        return dimensionValue;
    }

    public void setDimensionValue(String dimensionValue) {
        this.dimensionValue = dimensionValue;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public int getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(int monthCount) {
        this.monthCount = monthCount;
    }
}
