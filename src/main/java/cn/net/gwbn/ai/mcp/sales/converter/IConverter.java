package cn.net.gwbn.ai.mcp.sales.converter;

import java.util.List;

/**
 * @author lixiaofeng
 * @date 6/28/26 AM10:46
 **/
public interface IConverter<S, T> {

    /**
     * 单个对象转换
     */
    T convert(S source);

    /**
     * 列表对象转换
     */
    List<T> convertList(List<S> sourceList);
}