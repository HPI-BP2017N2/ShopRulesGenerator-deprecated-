package de.hpi.shoprulesgenerator.controller;

import de.hpi.shoprulesgenerator.dto.GenerateParameter;
import de.hpi.shoprulesgenerator.dto.GenerateResponse;
import de.hpi.shoprulesgenerator.model.data.ShopRule;
import de.hpi.shoprulesgenerator.model.data.ShopRuleRepository;
import de.hpi.shoprulesgenerator.service.ShopRulesGeneratorService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShopRulesGeneratorController {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private ShopRuleRepository repository;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private ShopRulesGeneratorService service;

    //initialization
    @Autowired
    public ShopRulesGeneratorController(ShopRulesGeneratorService service, ShopRuleRepository repository){
        setService(service);
        setRepository(repository);
    }

    //routes
    @RequestMapping(value = "/generate", method = RequestMethod.POST, produces = "application/json")
    public GenerateResponse generate(@RequestBody GenerateParameter parameter){
        ShopRule testRule = new ShopRule(System.currentTimeMillis(), "http://www.saturn.de/", "{}");
        getRepository().saveRuleForShop(testRule);
        return new GenerateResponse(getRepository().getRuleOfShop("http://www.saturn.de/").getShopRule());
    }

}
