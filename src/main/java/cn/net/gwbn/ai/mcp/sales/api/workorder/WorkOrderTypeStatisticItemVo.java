package cn.net.gwbn.ai.mcp.sales.api.workorder;

/**
 * 工单类型统计明细
 *
 * @author lixiaofeng
 * @date 9/22/26 PM4:16
 **/
public class WorkOrderTypeStatisticItemVo {


    /**
     * 工单类型
     */
    private String orderType;

    /**
     * 二级类型
     */
    private String secondType;

    /**
     * 三级类型
     */
    private String thirdType;

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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
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

    public String getSecondType() {
        return secondType;
    }

    public void setSecondType(String secondType) {
        this.secondType = secondType;
    }

    public String getThirdType() {
        return thirdType;
    }

    public void setThirdType(String thirdType) {
        this.thirdType = thirdType;
    }
}
