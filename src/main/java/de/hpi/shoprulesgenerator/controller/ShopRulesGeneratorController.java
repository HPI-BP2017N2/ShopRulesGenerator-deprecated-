package de.hpi.shoprulesgenerator.controller;

import de.hpi.restclient.dto.GenerateRulesResponse;
import de.hpi.restclient.dto.GetRulesResponse;
import de.hpi.restclient.pojo.Rules;
import de.hpi.shoprulesgenerator.service.ShopRulesGeneratorService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
public class ShopRulesGeneratorController {

    private RestTemplate restTemplate;
    private ShopRulesGeneratorService service;

    @Autowired
    public ShopRulesGeneratorController(ShopRulesGeneratorService service, RestTemplateBuilder restTemplateBuilder){
        setService(service);
        setRestTemplate(restTemplateBuilder.build());
    }

    @RequestMapping(value = "/generateRules", method = RequestMethod.GET)
    public void generateRules(@RequestParam(value="shopID") long shopID) {
        new Thread (() -> getService().generateForShop(shopID)).start();
    }

    @RequestMapping(value = "/getRules", method = RequestMethod.GET, produces = "application/json")
    public GetRulesResponse getRules(@RequestParam(value="shopID") long shopID) {
        GetRulesResponse response = new  GetRulesResponse();
        response.setRules(getService().getRulesForShop(shopID));
        return response;
    }

}
