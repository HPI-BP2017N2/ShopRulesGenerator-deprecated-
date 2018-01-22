package de.hpi.shoprulesgenerator.controller;

import de.hpi.restclient.dto.GetRulesResponse;
import de.hpi.shoprulesgenerator.service.ShopRulesGeneratorService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
public class ShopRulesGeneratorController {

    private ShopRulesGeneratorService service;

    @Autowired
    public ShopRulesGeneratorController(ShopRulesGeneratorService service){
        setService(service);
    }

    @RequestMapping(value = "/getRules", method = RequestMethod.GET, produces = "application/json")
    public GetRulesResponse get(@RequestParam(value="shopID") long shopID) {
        GetRulesResponse response = new GetRulesResponse();

        return response;
    }
}
