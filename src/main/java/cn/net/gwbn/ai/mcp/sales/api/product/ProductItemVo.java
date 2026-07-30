package cn.net.gwbn.ai.mcp.sales.api.product;

import java.math.BigDecimal;

/**
 * @author lixiaofeng
 * @date 6/29/26 AM8:52
 **/
public class ProductItemVo {


    /**
     * 规格
     */
    private String dimension;

    /**
     * 当月收入
     */
    private BigDecimal monthIncome;

    /**
     * 当日收入
     */
    private BigDecimal dayIncome;


    private String displayMonthIncome;
    private String displayDayIncome;


    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public BigDecimal getMonthIncome() {
        return monthIncome;
    }

    public void setMonthIncome(BigDecimal monthIncome) {
        this.monthIncome = monthIncome;
    }

    public BigDecimal getDayIncome() {
        return dayIncome;
    }

    public void setDayIncome(BigDecimal dayIncome) {
        this.dayIncome = dayIncome;
    }

    public String getDisplayMonthIncome() {
        return displayMonthIncome;
    }

    public void setDisplayMonthIncome(String displayMonthIncome) {
        this.displayMonthIncome = displayMonthIncome;
    }

    public String getDisplayDayIncome() {
        return displayDayIncome;
    }

    public void setDisplayDayIncome(String displayDayIncome) {
        this.displayDayIncome = displayDayIncome;
    }
}
