package cn.net.gwbn.ai.mcp.sales.api.workorder;

/**
 * @author lixiaofeng
 * @date 9/14/26 PM3:50
 **/
public class MassOrderStatisticItemVo {


    /**
     * 构造故障类型
     */
    private String faultType;

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

    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
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
}
