package cn.net.gwbn.ai.mcp.sales.api.user;

import cn.net.gwbn.ai.mcp.sales.api.SummaryVieObject;

import java.util.List;

/**
 * 续费率统计
 *
 * @author lixiaofeng
 * @date 7/28/26 PM3:14
 **/
public class RenewalRateSummaryVo extends SummaryVieObject {


    private int year;

    private int month;

    private int t;

    private List<RenewalRateItem> renewalRateList;

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

    public List<RenewalRateItem> getRenewalRateList() {
        return renewalRateList;
    }

    public void setRenewalRateList(List<RenewalRateItem> renewalRateList) {
        this.renewalRateList = renewalRateList;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }
}
