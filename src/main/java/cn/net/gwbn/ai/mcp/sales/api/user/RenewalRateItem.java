package cn.net.gwbn.ai.mcp.sales.api.user;

/**
 * 续费率明细
 *
 * @author lixiaofeng
 * @date 7/28/26 PM3:15
 **/
public class RenewalRateItem {


    private String cityId;

    private String cityName;

    private long totalRenewalCount;

    private long currentRenewalCount;

    private String renewalRate;

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

    public long getTotalRenewalCount() {
        return totalRenewalCount;
    }

    public void setTotalRenewalCount(long totalRenewalCount) {
        this.totalRenewalCount = totalRenewalCount;
    }

    public long getCurrentRenewalCount() {
        return currentRenewalCount;
    }

    public void setCurrentRenewalCount(long currentRenewalCount) {
        this.currentRenewalCount = currentRenewalCount;
    }

    public String getRenewalRate() {
        return renewalRate;
    }

    public void setRenewalRate(String renewalRate) {
        this.renewalRate = renewalRate;
    }
}
