package de.hpi.shoprulesgenerator.model;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

public class TextNodeSelectorGenerator extends NodeSelectorGenerator {

    @Override
    public List<Selector> generateSelectors(Document document, String offerAttribute) {
        List<Selector> selectors = new LinkedList<>();
        for (Element element : document.select("*:containsOwn(" + prepareAttribute(offerAttribute)+ ")")){
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

    //conditionals
    private boolean isElementIDSet(Element element) {
        return !element.id().isEmpty();
    }
}
