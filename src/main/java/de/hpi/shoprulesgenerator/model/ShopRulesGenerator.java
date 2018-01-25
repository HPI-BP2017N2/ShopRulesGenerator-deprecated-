package de.hpi.shoprulesgenerator.model;

import de.hpi.restclient.pojo.OfferAttribute;
import de.hpi.restclient.pojo.Rules;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.EnumMap;
import java.util.List;

@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
public class ShopRulesGenerator {

    public Rules generate(OfferFetcher offerFetcher, HTMLFetcher htmlFetcher,
                                 SelectorGenerator selectorGenerator) {
        EnumMap<OfferAttribute, AttributeEntry> attributeMap = fetchSelectors();
        EnumMap<OfferAttribute, List<String>> selectorMap = filterSelectors(attributeMap);
        return createRulesFromSelectors(selectorMap);
    }

    private Rules createRulesFromSelectors(EnumMap<OfferAttribute, List<String>> selectorMap) {
        return null;
    }

    private  EnumMap<OfferAttribute, List<String>> filterSelectors(EnumMap<OfferAttribute, AttributeEntry> attributeMap) {
        return null;
    }

    private  EnumMap<OfferAttribute,AttributeEntry> fetchSelectors() {
        
        return null;
    }

}