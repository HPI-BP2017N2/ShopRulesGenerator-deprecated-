package de.hpi.shoprulesgenerator.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
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

    public static class TextSelector extends Selector {
        public TextSelector(String selector) {
            super(Type.TEXT, selector);
        }
    }

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
    public static class AttributeSelector extends Selector {

        private String attributeKey;

        public AttributeSelector(String selector, String attributeKey) {
            super(Type.ATTRIBUTE, selector);
            setAttributeKey(attributeKey);
        }
    }

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
    public static class DataSelector extends Selector {

        private String mapKey;

        public DataSelector(String selector, String mapKey) {
            super(Type.DATA, selector);
            setMapKey(mapKey);
        }
    }
}
