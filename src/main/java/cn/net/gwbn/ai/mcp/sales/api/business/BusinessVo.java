package cn.net.gwbn.ai.mcp.sales.api.business;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

/**
 * @author lixiaofeng
 * @date 6/28/26 PM4:58
 **/
public class BusinessVo {


    /**
     * 业务名称
     */
    private String businessName;

    /**
     * 日收入
     */
    private BigDecimal dayIncome;

    private String displayDayIncome;

    private String displayMonthIncome;

    /**
     * 月收入
     */
    private BigDecimal monthIncome;


    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public BigDecimal getDayIncome() {
        return dayIncome;
    }

    public void setDayIncome(BigDecimal dayIncome) {
        this.dayIncome = dayIncome;
    }

    public String getDisplayDayIncome() {
        return displayDayIncome;
    }

    public void setDisplayDayIncome(String displayDayIncome) {
        this.displayDayIncome = displayDayIncome;
    }

    public String getDisplayMonthIncome() {
        return displayMonthIncome;
    }

    public void setDisplayMonthIncome(String displayMonthIncome) {
        this.displayMonthIncome = displayMonthIncome;
    }

    public BigDecimal getMonthIncome() {
        return monthIncome;
    }

    public void setMonthIncome(BigDecimal monthIncome) {
        this.monthIncome = monthIncome;
    }
}
