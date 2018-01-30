package de.hpi.shoprulesgenerator.model;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

public class SelectorGenerator {

    public List<String> getSelectorForOfferAttribute(Document html, String offerAttribute){
        offerAttribute = prepareAttribute(offerAttribute);
        List<String> selectors = new LinkedList<>();
        for (Element element : html.select("*:containsOwn(" + offerAttribute + ")")){
            selectors.add(getSelectorForDomElement(html, element));
        }
        return selectors;
    }

    public Element selectFirst(Document html, String selector) {
        Elements elements = html.select(selector);
        return (elements.isEmpty()) ? null : elements.get(0);
    }

    private String getSelectorForDomElement(Document html, Element element){
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
        return html
                .replace("\"", "\\\"")
                .replace("uml", "uml;");
    }

    //conditionals
    private boolean isElementIDSet(Element element) {
        return !element.id().isEmpty();
    }
}
