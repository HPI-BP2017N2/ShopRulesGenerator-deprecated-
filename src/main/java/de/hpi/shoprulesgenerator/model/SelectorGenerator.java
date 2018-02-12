package de.hpi.shoprulesgenerator.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;

import java.util.LinkedList;
import java.util.List;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class SelectorGenerator {

    @Getter(AccessLevel.PRIVATE) private final static String CONTENT_ATTRIBUTE = "content",
            ITEMPROP_ATTRIBUTE="itemprop";

    private List<NodeSelectorGenerator> nodeSelectorGenerators;

    public SelectorGenerator() {
        initializeNodeSelectorGenerators();
    }

    private void initializeNodeSelectorGenerators() {
        setNodeSelectorGenerators(new LinkedList<>());
        getNodeSelectorGenerators().add(new TextNodeSelectorGenerator());
        getNodeSelectorGenerators().add(new AttributeNodeSelectorGenerator());
    }

    public List<Selector> getSelectorsForOfferAttribute(Document html, String offerAttribute) {
        offerAttribute = offerAttribute.replace("uml", "uml;");
        List<Selector> selectors = new LinkedList<>();
        for (NodeSelectorGenerator selectorGenerator : getNodeSelectorGenerators()) {
            selectors.addAll(selectorGenerator.generateSelectors(html, offerAttribute));
        }
        return selectors;
    }
}
