package cn.net.gwbn.ai.mcp.engine.result;

/**
 * 列元数据
 *
 * @author lixiaofeng
 * @date 3/25/26 PM4:55
 **/
public class ColumnMeta {

    /**
     * 列名
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    public ColumnMeta(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }


    @Override
    public String toString() {
        return "ColumnMeta{name='" + name + '\'' + ", type='" + type + '\'' + '}';
    }
}
