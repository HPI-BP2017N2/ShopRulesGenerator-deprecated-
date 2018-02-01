package de.hpi.shoprulesgenerator.model;

import de.hpi.restclient.pojo.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;

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
        setFetchDelayInMs(fetchDelayInMS);
    }

    public Rules generate(Iterator<Offer> offerFetcher, HTMLFetcher htmlFetcher,
                          SelectorGenerator selectorGenerator) {
        EnumMap<OfferAttribute, AttributeEntry> attributeMap = addSelectorsForOffer(offerFetcher, selectorGenerator,
                htmlFetcher);
        EnumMap<OfferAttribute, List<String>> selectorMap = filterSelectors(attributeMap);
        return createRulesFromSelectors(selectorMap);
    }

    private Rules createRulesFromSelectors(EnumMap<OfferAttribute, List<String>> selectorMap) {
        Rules rules = new Rules();
        EnumMap<OfferAttribute, Rule> rulesMap = new EnumMap<>(OfferAttribute.class);
        for (Map.Entry<OfferAttribute, List<String>> selectorEntry : selectorMap.entrySet()) {
            Rule rule = new Rule(getRuleEntriesFromSelectors(selectorEntry.getValue()), selectorEntry.getKey());
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
                if (isMinConfidenceReached(selectorEntry.getValue(), attributeEntry.getValue().getAttributeValueFound())
                        && minimumAbsoluteAttributeCountReached(attributeEntry.getValue().getAttributeValueFound())) {
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

    private void addSelectorsForOffer(Offer offer, EnumMap<OfferAttribute, AttributeEntry> attributeMap,
                                      SelectorGenerator selectorGenerator, HTMLFetcher htmlFetcher) throws Exception {
        EnumMap<OfferAttribute, String> snapshot = offer.getOfferSnapshot();
        URL url = new URL(snapshot.get(OfferAttribute.URL));
        Document document = htmlFetcher.fetch(url);

        for (Map.Entry<OfferAttribute, String> offerAttribute : snapshot.entrySet()) {
            if (offerAttribute.getValue() == null) { continue; }
            List<String> selectors = selectorGenerator.getSelectorsForOfferAttribute(document, offerAttribute.getValue());
            updateAttributeEntry(attributeMap.get(offerAttribute.getKey()), selectors);
        }
    }

    private void updateAttributeEntry(AttributeEntry entry, List<String> selectors) {
        if (selectors.isEmpty()) { return; }
        entry.incrementAttributeValueFound();
        for (String selector : selectors) {
            if (!entry.getSelectorCountMap().containsKey(selector)){
                entry.getSelectorCountMap().put(selector, 0); //gets incremented in next step
            }
            entry.getSelectorCountMap().put(selector, entry.getSelectorCountMap().get(selector) + 1);
        }
    }

    //conversion
    private List<RuleEntry> getRuleEntriesFromSelectors(List<String> selectors){
        List<RuleEntry> ruleEntries = new LinkedList<>();
        for (String selector : selectors) {
            ruleEntries.add(new RuleEntry(selector));
        }
        return ruleEntries;
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

    private boolean isMinConfidenceReached(double selectorCount, double attributeValueFound) {
        return selectorCount / attributeValueFound * 100.0 >= getMinConfidence();
    }
}