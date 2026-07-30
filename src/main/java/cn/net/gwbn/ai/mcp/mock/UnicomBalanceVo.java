package cn.net.gwbn.ai.mcp.mock;

/**
 * @author lixiaofeng
 * @date 7/19/26 PM4:08
 **/
public class UnicomBalanceVo {


    /**
     * 手机号码
     */
    private String phoneNumber;


    /**
     * 当前余额(元)
     */
    private double balance;


    /**
     * 本月消费金额(元)
     */
    private double monthlyConsumption;


    /**
     * 当前套餐
     */
    private String packageName;


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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getMonthlyConsumption() {
        return monthlyConsumption;
    }

    public void setMonthlyConsumption(double monthlyConsumption) {
        this.monthlyConsumption = monthlyConsumption;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }
}