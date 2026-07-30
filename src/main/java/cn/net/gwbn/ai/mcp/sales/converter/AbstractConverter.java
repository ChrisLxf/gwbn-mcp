package cn.net.gwbn.ai.mcp.sales.converter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lixiaofeng
 * @date 6/28/26 AM10:47
 **/
public abstract class AbstractConverter<S, T> implements IConverter<S, T> {

    @Override
    public List<T> convertList(List<S> sourceList) {
        if (sourceList == null) {
            return null;
        }

        return sourceList.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    /**
     * 子类必须实现单对象转换逻辑
     *
     * @param source 源对象
     * @return 目标对象
     */
    @Override
    public abstract T convert(S source);
}