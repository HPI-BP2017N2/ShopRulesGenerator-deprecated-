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

    private  String prepareAttribute(String html) {
        return html
                .replace("\"", "\\\"")
                .replace("uml", "uml;");
    }

    private  String getSelectorForDomElement(Document html, Element element){
        StringBuilder xPathBuilder = new StringBuilder();
        while (!isElementIDSet(element) && element.parent() != null){
            int tagIndex = getTagIndexForChild(element.parent(), element);
            xPathBuilder.insert(0, " " + element.tagName() + ":eq(" + tagIndex + ")");
            element = element.parent();
        }
        xPathBuilder.insert(0,"*#" + element.id());
        return xPathBuilder.toString();
    }

    private  int getTagIndexForChild(Element parent, Element child){
        Elements childElementsWithTag = parent.getElementsByTag(child.tagName());
        for (int iElement = 0; iElement < childElementsWithTag.size(); iElement++){
            if (childElementsWithTag.get(iElement).equals(child)){
                return iElement;
            }
        }
        return -1;
    }

    private  boolean isElementIDSet(Element element) {
        return !element.id().isEmpty();
    }
}
