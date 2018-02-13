package de.hpi.shoprulesgenerator.service;

import de.hpi.restclient.clients.BPBridgeClient;
import de.hpi.restclient.clients.URLCleanerClient;
import de.hpi.restclient.pojo.Rules;
import de.hpi.shoprulesgenerator.model.*;
import de.hpi.shoprulesgenerator.properties.ShopRulesGeneratorSettingsProperties;
import de.hpi.shoprulesgenerator.repository.RulesDBEntry;
import de.hpi.shoprulesgenerator.repository.RulesRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
public class ShopRulesGeneratorService {

    private BPBridgeClient bpBridgeClient;
    private RulesRepository rulesRepository;
    private SelectorGenerator selectorGenerator;
    private ShopRulesGenerator shopRulesGenerator;
    private ShopRulesGeneratorSettingsProperties properties;
    private WebHTMLFetcher webHTMLFetcher;

    @Autowired
    public ShopRulesGeneratorService(ShopRulesGeneratorSettingsProperties properties, BPBridgeClient bpBridgeClient,
                                     URLCleanerClient urlCleanerClient, RulesRepository rulesRepository) {
        setBpBridgeClient(bpBridgeClient);
        setProperties(properties);
        setWebHTMLFetcher(new WebHTMLFetcher(new URLCleaner(urlCleanerClient), getProperties().getUserAgent()));
        setSelectorGenerator(new SelectorGenerator());
        setShopRulesGenerator(new ShopRulesGenerator(
                getProperties().getMaxOfferCount(),
                getProperties().getMinMatchCountPerAttribute(),
                getProperties().getMinConfidence(),
                getProperties().getFetchDelay()));
        setRulesRepository(rulesRepository);
    }

    public Rules getRulesForShop(long shopID) {
        RulesDBEntry entry = getRulesRepository().findByShopID(shopID);
        if (entry == null) { return null; }
        return entry.getRules();
    }

    public Rules generateForShop(long shopID) {
        OfferFetcher offerFetcher = new OfferFetcher(getBpBridgeClient(), shopID, getProperties().getPageSize());
        Rules generatedRules = getShopRulesGenerator().generate(offerFetcher, getWebHTMLFetcher(),
                getSelectorGenerator());
        getRulesRepository().save(new RulesDBEntry(shopID, generatedRules));
        System.out.println("saved " + generatedRules);
        return generatedRules;
    }

}
