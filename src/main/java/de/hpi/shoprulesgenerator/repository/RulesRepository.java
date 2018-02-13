package de.hpi.shoprulesgenerator.repository;

import de.hpi.restclient.pojo.Rules;
import de.hpi.restclient.pojo.Shop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RulesRepository extends MongoRepository<RulesDBEntry, Long>{

    RulesDBEntry findByShopID(long shopID);
}
