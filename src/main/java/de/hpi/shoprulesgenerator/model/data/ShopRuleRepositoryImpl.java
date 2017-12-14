package de.hpi.shoprulesgenerator.model.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class ShopRuleRepositoryImpl implements ShopRuleRepository {

    //constants
    @Getter(AccessLevel.PRIVATE) private static final String SHOP_RULE_COLLECTION_NAME = "shopRules";

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private MongoTemplate mongoTemplate;

    //initialization
    @Autowired
    public ShopRuleRepositoryImpl(MongoTemplate mongoTemplate) {
        setMongoTemplate(mongoTemplate);
    }

    @Override
    public ShopRule getRuleOfShop(String shopRootUrl) {
        Query findShopQuery = new Query();
        findShopQuery.addCriteria(Criteria.where("shopRootUrl").is(shopRootUrl));
        return getMongoTemplate().findOne(findShopQuery, ShopRule.class, getSHOP_RULE_COLLECTION_NAME());
    }

    @Override
    public void saveRuleForShop(ShopRule rule) {
        getMongoTemplate().save(rule);
    }
}
