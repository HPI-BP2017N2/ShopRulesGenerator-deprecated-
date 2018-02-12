package de.hpi.shoprulesgenerator.model;

import lombok.AccessLevel;
import lombok.Getter;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedList;
import java.util.List;

public class AttributeNodeSelectorGenerator extends NodeSelectorGenerator {

    @Getter(AccessLevel.PRIVATE) private final static String CONTENT_ATTRIBUTE = "content",
            ITEMPROP_ATTRIBUTE="itemprop";

    /**
     * @param document - The whole html document
     * @param offerAttribute - The string representation of an offer attribute value (f.e. name, price, description)
     * @return A list of selectors, where the attribute value is contained in an DOM-attribute node as value
     */
    @Override
    public List<Selector> generateSelectors(Document document, String offerAttribute) {
        List<Selector> selectors = new LinkedList<>();
        for (Element element : document.select(buildSelector(offerAttribute))){
            String attributeKey = getAttributeKeyByValue(element, offerAttribute);
            if (attributeKey != null && validateMatch(offerAttribute, element.attr(attributeKey))) {
                selectors.add(new Selector.AttributeSelector(getSelectorForAttributeNode(element), attributeKey));
            }
        }
        return selectors;
    }

    /**
     * @param offerAttribute - The string representation of an offer attribute value (f.e. name, price, description)
     * @return A selector that matches all attribute-pairs where the attribute key is CONTENT_ATTRIBUTE and the value
     * contains the offerAttribute.
     */
    private String buildSelector(String offerAttribute) {
        return "*[" + getCONTENT_ATTRIBUTE() + "*=" + prepareAttribute(offerAttribute) + "]";
    }

    /**
     * This method does not guarantee uniqueness of the selector, meaning that it can match other DOM-elements, too.
     * @param element - The DOM-element where the desired attribute key-value-pair is contained
     * @return - A selector to extract the dom element.
     */
    private String getSelectorForAttributeNode(Element element) {
        StringBuilder attributeSelectors = new StringBuilder();
        if (element.hasAttr(getITEMPROP_ATTRIBUTE())){
            attributeSelectors.append("[").append(getITEMPROP_ATTRIBUTE()).append("=")
                    .append(element.attr(getITEMPROP_ATTRIBUTE())).append("]");
        } else {
            for (Attribute attribute : element.attributes()) {
                if (!attribute.getKey().equalsIgnoreCase(getCONTENT_ATTRIBUTE())){
                    attributeSelectors.append("[").append(attribute.getKey()).append("=").append(attribute.getValue()
                    ).append("]");
                }
            }
        }
        return element.tagName() + attributeSelectors.toString();
    }

    private String getAttributeKeyByValue(Element element, String value) {
        for (Attribute attribute : element.attributes()){
            if (attribute.getValue().toLowerCase().contains(value.toLowerCase())){
                return attribute.getKey();
            }
        }
        return null;
    }

}
