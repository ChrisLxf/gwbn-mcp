package cn.net.gwbn.ai.mcp.sales.repository;

import cn.net.gwbn.ai.mcp.sales.model.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lixiaofeng
 * @date 7/6/26 PM5:00
 **/
@Repository
public interface CityRepository extends JpaRepository<CityEntity, String> {


    List<CityEntity>  findCityEntitiesByEnableAndType(boolean enable, String type);
}
