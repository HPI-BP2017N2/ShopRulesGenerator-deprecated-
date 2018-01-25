package de.hpi.shoprulesgenerator.service;

import de.hpi.restclient.clients.BPBridgeClient;
import de.hpi.restclient.pojo.OfferAttribute;
import de.hpi.restclient.pojo.Rules;
import de.hpi.shoprulesgenerator.model.OfferFetcher;
import de.hpi.shoprulesgenerator.model.SelectorGenerator;
import de.hpi.shoprulesgenerator.model.ShopRulesGenerator;
import de.hpi.shoprulesgenerator.model.WebHTMLFetcher;
import de.hpi.shoprulesgenerator.properties.ShopRulesGeneratorProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;

@Service
@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
public class ShopRulesGeneratorService {

    private BPBridgeClient bpBridgeClient;
    private SelectorGenerator selectorGenerator;
    private ShopRulesGenerator shopRulesGenerator;
    private ShopRulesGeneratorProperties properties;
    private WebHTMLFetcher webHTMLFetcher;

    @Autowired
    public ShopRulesGeneratorService(ShopRulesGeneratorProperties properties, BPBridgeClient bpBridgeClient) {
        setBpBridgeClient(bpBridgeClient);
        setProperties(properties);
        setWebHTMLFetcher(new WebHTMLFetcher(getProperties().getUserAgent()));
        setSelectorGenerator(new SelectorGenerator());
        setShopRulesGenerator(new ShopRulesGenerator(
                getProperties().getMaxOfferCount(),
                getProperties().getMinMatchCountPerAttribute(),
                getProperties().getMinMatchRatePerAttribute(),
                getProperties().getMinConfidence(),
                getProperties().getFetchDelay()));
    }

    public Rules generateForShop(long shopID) {
        OfferFetcher offerFetcher = new OfferFetcher(getBpBridgeClient(), shopID, getProperties().getPageSize());
        return getShopRulesGenerator().generate(offerFetcher, getWebHTMLFetcher(), getSelectorGenerator());
    }

}
