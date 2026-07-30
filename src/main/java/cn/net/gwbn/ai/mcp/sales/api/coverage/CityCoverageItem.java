package cn.net.gwbn.ai.mcp.sales.api.coverage;

/**
 * @author lixiaofeng
 * @date 7/30/26 PM3:10
 **/
public class CityCoverageItem {


    private String cityName;

    private String cityId;

    private long coverageNumber;


    private String unit;


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public long getCoverageNumber() {
        return coverageNumber;
    }

    public void setCoverageNumber(long coverageNumber) {
        this.coverageNumber = coverageNumber;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
