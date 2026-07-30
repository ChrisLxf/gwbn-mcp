package cn.net.gwbn.ai.mcp.configurattion;

import cn.net.gwbn.ai.mcp.mcp.*;
import cn.net.gwbn.ai.mcp.sales.command.*;
import cn.net.gwbn.ai.mcp.sales.converter.business.BusinessIncomeSummaryVoConverter;
import cn.net.gwbn.ai.mcp.sales.converter.business.BusinessIncomeSummaryVoConverterImpl;
import cn.net.gwbn.ai.mcp.sales.converter.income.CityIncomeSummaryVoConverter;
import cn.net.gwbn.ai.mcp.sales.converter.income.CityIncomeSummaryVoConverterImpl;
import cn.net.gwbn.ai.mcp.engine.QueryEngine;
import cn.net.gwbn.ai.mcp.sales.converter.product.ProductSummaryVoConverter;
import cn.net.gwbn.ai.mcp.sales.converter.product.ProductSummaryVoConverterImpl;
import cn.net.gwbn.ai.mcp.sales.converter.user.ExistUserSummaryVoConverter;
import cn.net.gwbn.ai.mcp.sales.converter.user.ExistUserSummaryVoConverterImpl;
import cn.net.gwbn.ai.mcp.sales.converter.user.RenewalRateSummaryVoConverter;
import cn.net.gwbn.ai.mcp.sales.converter.user.RenewalRateSummaryVoConverterImpl;
import cn.net.gwbn.ai.mcp.sales.repository.CityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author lixiaofeng
 * @date 6/28/26 AM11:43
 **/
@Configuration
public class GwbnMcpAutoConfiguration {

    @Bean
    public QueryEngine queryEngine(JdbcTemplate jdbcTemplate) {
        return new QueryEngine(jdbcTemplate);
    }

    @Bean
    public CityIncomeSummaryVoConverter cityIncomeSummaryVoConverter() {
        return new CityIncomeSummaryVoConverterImpl();
    }

    @Bean
    public CityIncomeSummaryCommand cityIncomeSummaryCommand(QueryEngine queryEngine, CityIncomeSummaryVoConverter cityIncomeSummaryVoConverter, CityRepository cityRepository) {
        return new CityIncomeSummaryCommand(queryEngine, cityIncomeSummaryVoConverter, cityRepository);
    }

    @Bean
    public CityIncomeSummaryMcpClient cityIncomeSummaryMcpClient(CityIncomeSummaryCommand cityIncomeSummaryCommand) {
        return new CityIncomeSummaryMcpClient(cityIncomeSummaryCommand);
    }

    @Bean
    public BusinessIncomeSummaryVoConverter businessIncomeSummaryVoConverter() {
        return new BusinessIncomeSummaryVoConverterImpl();
    }

    @Bean
    public BusinessSummaryCommand businessSummaryCommand(QueryEngine queryEngine, BusinessIncomeSummaryVoConverter businessIncomeSummaryVoConverter, CityRepository cityRepository) {
        return new BusinessSummaryCommand(queryEngine, businessIncomeSummaryVoConverter, cityRepository);
    }

    @Bean
    public BusinessSummaryMcpClient businessSummaryMcpClient(BusinessSummaryCommand businessSummaryCommand) {
        return new BusinessSummaryMcpClient(businessSummaryCommand);
    }


    @Bean
    public ProductSummaryVoConverter productSummaryVoConverter() {
        return new ProductSummaryVoConverterImpl();
    }

    @Bean
    public ProductSummaryCommand productSummaryCommand(QueryEngine queryEngine, ProductSummaryVoConverter productSummaryVoConverter, CityRepository cityRepository) {
        return new ProductSummaryCommand(queryEngine, productSummaryVoConverter, cityRepository);
    }

    @Bean
    public ProductSummaryMcpClient productSummaryMcpClient(ProductSummaryCommand productSummaryCommand) {
        return new ProductSummaryMcpClient(productSummaryCommand);
    }


    @Bean
    public ExistUserSummaryVoConverter existUserSummaryVoConverter() {
        return new ExistUserSummaryVoConverterImpl();
    }

    @Bean
    public ExistUserSummaryCommand existUserSummaryCommand(QueryEngine queryEngine, ExistUserSummaryVoConverter existUserSummaryVoConverter) {
        return new ExistUserSummaryCommand(queryEngine, existUserSummaryVoConverter);
    }

    @Bean
    public ExistUserSummaryMcpClient existUserSummaryMcpClient(ExistUserSummaryCommand existUserSummaryCommand) {
        return new ExistUserSummaryMcpClient(existUserSummaryCommand);
    }

    @Bean
    public RenewalRateSummaryVoConverter renewalRateSummaryVoConverter() {
        return new RenewalRateSummaryVoConverterImpl();
    }

    @Bean
    public RenewalCommand renewalCommand(QueryEngine queryEngine, RenewalRateSummaryVoConverter renewalRateSummaryVoConverter) {
        return new RenewalCommand(queryEngine, renewalRateSummaryVoConverter);
    }

    @Bean
    public RenewalRateSummaryMcpClient renewalRateSummaryMcpClient(RenewalCommand renewalCommand) {
        return new RenewalRateSummaryMcpClient(renewalCommand);
    }
}
