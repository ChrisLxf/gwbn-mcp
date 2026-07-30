package cn.net.gwbn.ai.mcp.sales.converter.product;

import cn.net.gwbn.ai.mcp.engine.result.QueryResult;
import cn.net.gwbn.ai.mcp.sales.api.income.CityIncomeSummaryVo;
import cn.net.gwbn.ai.mcp.sales.api.product.ProductSummaryVo;
import cn.net.gwbn.ai.mcp.sales.converter.IConverter;

/**
 * @author lixiaofeng
 * @date 6/29/26 AM8:59
 **/
public interface ProductSummaryVoConverter extends IConverter<QueryResult, ProductSummaryVo> {


    ProductSummaryVo convert(int year, int month, int day, String dim,String type, QueryResult dayResult, QueryResult monthResult);

}
