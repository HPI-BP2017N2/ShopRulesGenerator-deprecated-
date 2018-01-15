package de.hpi.shoprulesgenerator.service;

import de.hpi.shoprulesgenerator.model.ShopRulesGenerator;
import de.hpi.shoprulesgenerator.model.data.Rule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
public class ShopRulesGeneratorService {

    private ShopRulesGenerator generator;

    public ShopRulesGeneratorService() {
        setGenerator(new ShopRulesGenerator());
    }

    public HashMap<String, List<Rule>> getRulesForShop(long shopID) {
        return null;
    }
}
