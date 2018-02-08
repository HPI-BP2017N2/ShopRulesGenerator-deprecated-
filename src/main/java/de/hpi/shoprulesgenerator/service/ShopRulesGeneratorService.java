package de.hpi.shoprulesgenerator.service;

import de.hpi.restclient.clients.BPBridgeClient;
import de.hpi.restclient.clients.URLCleanerClient;
import de.hpi.restclient.pojo.Rules;
import de.hpi.shoprulesgenerator.model.*;
import de.hpi.shoprulesgenerator.properties.ShopRulesGeneratorProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
public class ShopRulesGeneratorService {

    private BPBridgeClient bpBridgeClient;
    private SelectorGenerator selectorGenerator;
    private ShopRulesGenerator shopRulesGenerator;
    private ShopRulesGeneratorProperties properties;
    private WebHTMLFetcher webHTMLFetcher;

    @Autowired
    public ShopRulesGeneratorService(ShopRulesGeneratorProperties properties, BPBridgeClient bpBridgeClient,
                                     URLCleanerClient urlCleanerClient) {
        setBpBridgeClient(bpBridgeClient);
        setProperties(properties);
        setWebHTMLFetcher(new WebHTMLFetcher(new URLCleaner(urlCleanerClient), getProperties().getUserAgent()));
        setSelectorGenerator(new SelectorGenerator());
        setShopRulesGenerator(new ShopRulesGenerator(
                getProperties().getMaxOfferCount(),
                getProperties().getMinMatchCountPerAttribute(),
                getProperties().getMinConfidence(),
                getProperties().getFetchDelay()));
    }

    public Rules generateForShop(long shopID) {
        OfferFetcher offerFetcher = new OfferFetcher(getBpBridgeClient(), shopID, getProperties().getPageSize());
        return getShopRulesGenerator().generate(offerFetcher, getWebHTMLFetcher(), getSelectorGenerator());
    }

}
