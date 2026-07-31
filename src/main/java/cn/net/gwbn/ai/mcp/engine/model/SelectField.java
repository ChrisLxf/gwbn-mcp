package cn.net.gwbn.ai.mcp.engine.model;

/**
 * select字段
 *
 * @author lixiaofeng
 * @date 7/30/26 PM3:23
 **/
public class SelectField {


    private String column;


    public SelectField(String column) {
        this.column = column;
    }


    public String getColumn() {
        return column;
    }
}