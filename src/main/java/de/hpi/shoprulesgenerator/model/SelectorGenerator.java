package de.hpi.shoprulesgenerator.model;

import lombok.AccessLevel;
import lombok.Getter;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

public class SelectorGenerator {

    @Getter(AccessLevel.PRIVATE) private final static String CONTENT_ATTRIBUTE = "content",
            ITEMPROP_ATTRIBUTE="itemprop";

    public List<Selector> getSelectorsForOfferAttribute(Document html, String offerAttribute) {
        offerAttribute = offerAttribute.replace("uml", "uml;");
        List<Selector> selectors = new LinkedList<>();
        selectors.addAll(getTextNodeSelectors(html, offerAttribute));
        selectors.addAll(getAttributeNodeSelectors(html, offerAttribute));
        return selectors;
    }

    /**
     * @param html - The whole html document
     * @param offerAttribute - The string representation of an offer attribute value (f.e. name, price, description)
     * @return A list of selectors, where the attribute value is contained in an DOM-attribute node as value
     */
    private List<Selector> getAttributeNodeSelectors(Document html, String offerAttribute) {
        List<Selector> selectors = new LinkedList<>();
        for (Element element : html.select("*[" + getCONTENT_ATTRIBUTE() + "*=" + prepareAttribute(offerAttribute) +
                "]")){
            String attributeKey = getAttributeKeyByValue(element, offerAttribute);
            if (attributeKey != null && validateMatch(offerAttribute, element.attr(attributeKey))) {
                selectors.add(new Selector.AttributeSelector(getSelectorForAttributeNode(element), attributeKey));
            }
        }
        return selectors;
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

    /**
     * @param html - The whole html document
     * @param offerAttribute - The string representation of an offer attribute value (f.e. name, price, description)
     * @return A list of selectors, where the attribute value is contained between two HTML-Tags
     */
    private List<Selector> getTextNodeSelectors(Document html, String offerAttribute){
        List<Selector> selectors = new LinkedList<>();
        for (Element element : html.select("*:containsOwn(" + prepareAttribute(offerAttribute)+ ")")){
            if (validateMatch(element.text(), offerAttribute)) {
                selectors.add(new Selector.TextSelector(getSelectorForDomElement(element)));
            }
        }
        return selectors;
    }

    /**
     * @param element - The target DOM-Element
     * @return A minimal selector with highest possible identity match to select the element out of html.
     */
    private String getSelectorForDomElement(Element element){
        StringBuilder selectorBuilder = new StringBuilder();
        while (!isElementIDSet(element) && element.parent() != null){
            int tagIndex = getTagIndexForChild(element);
            selectorBuilder.insert(0, " " + element.tagName() + ":nth-of-type(" + tagIndex + ")");
            element = element.parent();
        }
        if (isElementIDSet(element)){
            selectorBuilder.insert(0,"#" + element.id());
        } else { //element.parent() == null
            selectorBuilder.deleteCharAt(0);
        }
        return selectorBuilder.toString();
    }

    /**
     * @param child The child element, that's tag index should get returned for
     * @return A 1-based index indicating umpteenth child this is of its own parent with the same tag as the child.
     */
    private int getTagIndexForChild(Element child){
        Element parent = child.parent();
        if (parent == null) { return -1; }
        Elements childElementsWithTag = parent.getElementsByTag(child.tagName());
        for (int iElement = 0; iElement < childElementsWithTag.size(); iElement++){
            if (childElementsWithTag.get(iElement).equals(child)){
                if (parent.tagName().equals(child.tagName())){ //resolving an unexpected behaviour of JSoup
                    iElement--;
                }
                return iElement + 1;
            }
        }
        return -1;
    }

    //conversion
    private String prepareAttribute(String html) {
        return html.replace("\"", "\\\"");
    }

    //conditionals
    private boolean isElementIDSet(Element element) {
        return !element.id().isEmpty();
    }

    private boolean validateMatch(String offerAttribute, String value) {
        String cleanedValue = removePreAndPostSpaces(value);
        return cleanedValue.equalsIgnoreCase(offerAttribute);
    }

    private String removePreAndPostSpaces(String value) {
        StringBuilder valueBuilder = new StringBuilder(value);
        int index = -1;
        while ((index = valueBuilder.indexOf(" ")) == 0 || index == valueBuilder.length() - 1){
            valueBuilder.deleteCharAt(value.indexOf(index));
        }
        return valueBuilder.toString();
    }
}
