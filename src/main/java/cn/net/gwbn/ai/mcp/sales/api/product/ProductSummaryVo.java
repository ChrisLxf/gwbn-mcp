package cn.net.gwbn.ai.mcp.sales.api.product;

import cn.net.gwbn.ai.mcp.sales.api.SummaryVieObject;
import cn.net.gwbn.ai.mcp.sales.api.business.BusinessVo;

import java.util.List;

/**
 * 产品统计展示类
 *
 * @author lixiaofeng
 * @date 6/29/26 AM8:51
 **/
public class ProductSummaryVo extends SummaryVieObject {

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


    /**
     * 维度
     */
    private String dimension;

    /**
     * 维度描述
     */
    private String dimensionDescription;

    /**
     *
     */
    private String unit;


    private List<ProductItemVo> productItems;


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

    public List<ProductItemVo> getProductItems() {
        return productItems;
    }

    public void setProductItems(List<ProductItemVo> productItems) {
        this.productItems = productItems;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    public String getDimensionDescription() {
        return dimensionDescription;
    }

    public void setDimensionDescription(String dimensionDescription) {
        this.dimensionDescription = dimensionDescription;
    }
}
