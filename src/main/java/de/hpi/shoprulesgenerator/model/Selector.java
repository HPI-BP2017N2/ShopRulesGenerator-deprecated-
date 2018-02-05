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

    protected String selector;
    protected Type type;

    private Selector(Type type, String selector){
        setType(type);
        setSelector(selector);
    }

    @EqualsAndHashCode(callSuper = true)
    static class TextSelector extends Selector {
        TextSelector(String selector) {
            super(Type.TEXT, selector);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Getter @Setter
    static class AttributeSelector extends Selector {

        private String attributeKey;

        AttributeSelector(String selector, String attributeKey) {
            super(Type.ATTRIBUTE, selector);
            setAttributeKey(attributeKey);
        }
    }

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
