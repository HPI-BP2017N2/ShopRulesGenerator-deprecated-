package de.hpi.shoprulesgenerator.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class Selector {

    public enum Type {
        TEXT,
        ATTRIBUTE,
        DATA
    }

    private String selector;
    private Type type;

    private Selector(Type type, String selector){
        setType(type);
        setSelector(selector);
    }

    @ToString
    @EqualsAndHashCode(callSuper = true)
    static class TextSelector extends Selector {
        TextSelector(String selector) {
            super(Type.TEXT, selector);
        }
    }

    @ToString
    @EqualsAndHashCode(callSuper = true)
    @Getter @Setter
    static class AttributeSelector extends Selector {

        private String attributeKey;

        AttributeSelector(String selector, String attributeKey) {
            super(Type.ATTRIBUTE, selector);
            setAttributeKey(attributeKey);
        }
    }

    @ToString
    @EqualsAndHashCode(callSuper = true)
    @Getter @Setter
    static class DataSelector extends Selector {

        private String mapKey;

        DataSelector(String selector, String mapKey) {
            super(Type.DATA, selector);
            setMapKey(mapKey);
        }
    }
}
