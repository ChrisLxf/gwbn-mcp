package cn.net.gwbn.ai.mcp.mock;

/**
 * @author lixiaofeng
 * @date 7/19/26 PM4:06
 **/
public class UnicomTrafficVo {


    /**
     * 手机号码
     */
    private String phoneNumber;


    /**
     * 套餐总流量 GB
     */
    private double totalTraffic;


    /**
     * 已使用流量 GB
     */
    private double usedTraffic;


    /**
     * 剩余流量 GB
     */
    private double remainTraffic;


    /**
     * 5G专属流量 GB
     */
    private double fiveGTraffic;


    /**
     * 查询时间
     */
    private String queryTime;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getTotalTraffic() {
        return totalTraffic;
    }

    public void setTotalTraffic(double totalTraffic) {
        this.totalTraffic = totalTraffic;
    }

    public double getUsedTraffic() {
        return usedTraffic;
    }

    public void setUsedTraffic(double usedTraffic) {
        this.usedTraffic = usedTraffic;
    }

    public double getRemainTraffic() {
        return remainTraffic;
    }

    public void setRemainTraffic(double remainTraffic) {
        this.remainTraffic = remainTraffic;
    }

    public double getFiveGTraffic() {
        return fiveGTraffic;
    }

    public void setFiveGTraffic(double fiveGTraffic) {
        this.fiveGTraffic = fiveGTraffic;
    }

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }
}