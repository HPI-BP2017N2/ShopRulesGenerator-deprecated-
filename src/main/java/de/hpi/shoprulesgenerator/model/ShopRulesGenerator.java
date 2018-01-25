package de.hpi.shoprulesgenerator.model;

import de.hpi.restclient.pojo.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
public class ShopRulesGenerator {

    private int maxOfferCount, minMatchCountPerAttribute, minConfidence, fetchDelayInMs;

    public ShopRulesGenerator(int maxOfferCount, int minMatchCountPerAttribute, int
            minConfidence, int fetchDelayInMS) {
        setMaxOfferCount(maxOfferCount);
        setMinMatchCountPerAttribute(minMatchCountPerAttribute);
        setMinConfidence(minConfidence);
    }

    public Rules generate(Iterator<Offer> offerFetcher, HTMLFetcher htmlFetcher,
                          SelectorGenerator selectorGenerator) {
        EnumMap<OfferAttribute, AttributeEntry> attributeMap = addSelectorsForOffer(offerFetcher, selectorGenerator,
                htmlFetcher);
        EnumMap<OfferAttribute, List<String>> selectorMap = filterSelectors(attributeMap);
        return createRulesFromSelectors(selectorMap);
    }

    //this one needs refactoring
    private Rules createRulesFromSelectors(EnumMap<OfferAttribute, List<String>> selectorMap) {
        Rules rules = new Rules();
        EnumMap<OfferAttribute, Rule> rulesMap = new EnumMap<>(OfferAttribute.class);
        for (Map.Entry<OfferAttribute, List<String>> selectorEntry : selectorMap.entrySet()) {
            Rule rule = new Rule();
            rule.setAttribute(selectorEntry.getKey());
            List<RuleEntry> ruleEntries = new LinkedList<>();
            for (String selector : selectorEntry.getValue()) {
                RuleEntry ruleEntry = new RuleEntry();
                ruleEntry.setSelector(selector);
                ruleEntry.setAttribute(null);
                ruleEntry.setResultAsPlainText(true);
                ruleEntries.add(ruleEntry);
            }
            rule.setEntries(ruleEntries);
            rulesMap.put(selectorEntry.getKey(), rule);
        }
        rules.setRules(rulesMap);
        return rules;
    }

    private EnumMap<OfferAttribute, List<String>> filterSelectors(EnumMap<OfferAttribute, AttributeEntry> attributeMap) {
        EnumMap<OfferAttribute, List<String>> selectorMap = new EnumMap<>(OfferAttribute.class);
        for (Map.Entry<OfferAttribute, AttributeEntry> attributeEntry : attributeMap.entrySet()) {
            List<String> filteredSelectors = new LinkedList<>();
            for (Map.Entry<String, Integer> selectorEntry : attributeEntry.getValue().getSelectorCountMap().entrySet()) {
                if ((double) selectorEntry.getValue() / attributeEntry.getValue().getAttributeValueFound() * 100.0 >=
                        getMinConfidence()) {
                    filteredSelectors.add(selectorEntry.getKey());
                }
            }
            selectorMap.put(attributeEntry.getKey(), filteredSelectors);
        }
        return selectorMap;
    }

    private EnumMap<OfferAttribute,AttributeEntry> addSelectorsForOffer(Iterator<Offer> offerFetcher, SelectorGenerator selectorGenerator, HTMLFetcher htmlFetcher) {
        EnumMap<OfferAttribute, AttributeEntry> attributeMap = createEmptyAttributeMap();
        for (int iOffer = 0;
             iOffer < getMaxOfferCount() && requiresMoreOffers(attributeMap) && offerFetcher.hasNext();
             iOffer++) {
            try {
                addSelectorsForOffer(offerFetcher.next(), attributeMap, selectorGenerator, htmlFetcher);
                Thread.sleep(getFetchDelayInMs());
            } catch (Exception ignored) {}
        }
        return attributeMap;
    }

    private EnumMap<OfferAttribute, AttributeEntry> createEmptyAttributeMap() {
        EnumMap<OfferAttribute, AttributeEntry> attributeMap = new EnumMap<>(OfferAttribute.class);
        for (OfferAttribute attribute : OfferAttribute.values()) {
            attributeMap.put(attribute, new AttributeEntry());
        }
        return attributeMap;
    }

    private void addSelectorsForOffer(Offer offer, EnumMap<OfferAttribute, AttributeEntry> attributeMap, SelectorGenerator selectorGenerator, HTMLFetcher htmlFetcher) throws NoSuchFieldException, IllegalAccessException, IOException, URISyntaxException {
        EnumMap<OfferAttribute, String> snapshot = offer.getOfferSnapshot();
        URL url = new URL(snapshot.get(OfferAttribute.URL));
        Document document = htmlFetcher.fetch(url);
        for (Map.Entry<OfferAttribute, String> offerAttribute : snapshot.entrySet()) {
            if (offerAttribute.getValue() == null) {
                continue;
            }
            List<String> selectors = selectorGenerator.getSelectorForOfferAttribute(document, offerAttribute.getValue());
            updateAttributeEntry(attributeMap.get(offerAttribute.getKey()), selectors);
        }
    }

    private void updateAttributeEntry(AttributeEntry entry, List<String> selectors) {
        if (selectors.isEmpty()) {
            return;
        }
        entry.incrementAttributeValueFound();
        for (String selector : selectors) {
            if (!entry.getSelectorCountMap().containsKey(selector)){
                entry.getSelectorCountMap().put(selector, 1);
            }
            entry.getSelectorCountMap().put(selector, entry.getSelectorCountMap().get(selector) + 1);
        }
    }

    //conditionals
    private boolean requiresMoreOffers(EnumMap<OfferAttribute, AttributeEntry> attributeMap) {
        for (AttributeEntry entry : attributeMap.values()) {
            if (!minimumAbsoluteAttributeCountReached(entry.getAttributeValueFound())){
                return true;
            }
        }
        return false;
    }

    private boolean minimumAbsoluteAttributeCountReached(int attributeCount) {
        return attributeCount >= getMinMatchCountPerAttribute();
    }
}