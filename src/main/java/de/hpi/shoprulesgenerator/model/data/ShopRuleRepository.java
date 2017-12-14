package de.hpi.shoprulesgenerator.model.data;

import org.springframework.stereotype.Repository;

@Repository
public interface ShopRuleRepository {

    public ShopRule getRuleOfShop(String shopRootUrl);

    public void saveRuleForShop(ShopRule rule);
}
