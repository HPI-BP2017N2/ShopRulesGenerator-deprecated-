package de.hpi.shoprulesgenerator.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Selector)) { return false; }
        Selector otherSelector = (Selector) o;
        return Objects.equals(getSelector(), otherSelector.getSelector()) &&
                Objects.equals(getType(), otherSelector.getType());
    }

    static class TextSelector extends Selector {
        TextSelector(String selector) {
            super(Type.TEXT, selector);
        }
    }

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
    static class AttributeSelector extends Selector {

        private String attributeKey;

        AttributeSelector(String selector, String attributeKey) {
            super(Type.ATTRIBUTE, selector);
            setAttributeKey(attributeKey);
        }

        @Override
        public boolean equals(Object o) {
            return super.equals(o) &&
                    Objects.equals(getAttributeKey(), ((AttributeSelector) o).getAttributeKey());
        }
    }

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
    static class DataSelector extends Selector {

        private String mapKey;

        DataSelector(String selector, String mapKey) {
            super(Type.DATA, selector);
            setMapKey(mapKey);
        }

        @Override
        public boolean equals(Object o) {
            return super.equals(o) &&
                    Objects.equals(getMapKey(), ((DataSelector) o).getMapKey());
        }
    }
}
