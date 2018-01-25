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

    public static Rules generate(OfferFetcher offerFetcher, HTMLFetcher htmlFetcher,
                                 SelectorGenerator selectorGenerator) {

        return null;
    }
}