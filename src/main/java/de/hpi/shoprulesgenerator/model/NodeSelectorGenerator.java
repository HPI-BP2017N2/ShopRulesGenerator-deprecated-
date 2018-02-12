package de.hpi.shoprulesgenerator.model;

import org.jsoup.nodes.Document;

import java.util.List;

abstract class NodeSelectorGenerator {

    public abstract List<Selector> generateSelectors(Document document, String offerAttribute);

    //conditionals
    boolean validateMatch(String offerAttribute, String value) {
        String cleanedValue = removePreAndPostSpaces(value);
        return cleanedValue.equalsIgnoreCase(offerAttribute);
    }

    //conversion
    String prepareAttribute(String html) {
        return html.replace("\"", "\\\"");
    }

    private String removePreAndPostSpaces(String value) {
        StringBuilder valueBuilder = new StringBuilder(value);
        int index;
        while ((index = valueBuilder.indexOf(" ")) == 0 || index == valueBuilder.length() - 1){
            valueBuilder.deleteCharAt(value.indexOf(index));
        }
        return valueBuilder.toString();
    }


}
