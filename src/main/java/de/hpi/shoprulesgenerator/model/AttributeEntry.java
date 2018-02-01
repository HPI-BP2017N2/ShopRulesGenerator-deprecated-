package de.hpi.shoprulesgenerator.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

@ToString
@Getter @Setter
public class AttributeEntry {

    private HashMap<Selector, Integer> selectorCountMap;
    private int attributeValueFound;

    AttributeEntry() {
        setAttributeValueFound(0);
        setSelectorCountMap(new HashMap<>());
    }

    public void incrementAttributeValueFound() {
        setAttributeValueFound(getAttributeValueFound() + 1);
    }
}
