package de.hpi.shoprulesgenerator.service;

import de.hpi.shoprulesgenerator.model.ShopRulesGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
public class ShopRulesGeneratorService {

    private ShopRulesGenerator generator;

    public ShopRulesGeneratorService() {
        setGenerator(new ShopRulesGenerator());
    }

}
