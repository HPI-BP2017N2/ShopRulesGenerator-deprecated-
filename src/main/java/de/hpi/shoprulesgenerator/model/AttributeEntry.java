package de.hpi.shoprulesgenerator.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter @Setter
public class AttributeEntry {

    private HashMap<String, Integer> selectorCountMap;
    private int attributeValueFound;

    private AttributeEntry() {
        setAttributeValueFound(0);
        setSelectorCountMap(new HashMap<>());
    }
}
