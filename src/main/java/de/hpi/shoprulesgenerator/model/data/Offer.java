package de.hpi.shoprulesgenerator.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class Offer {

    private List<String> categoryPaths;
    private Map<String, Number> price;
    private Map<String, String> description, offerTitle, url;
    private Number shopId;
    private String currency, sku, han, brandSearchtext, categoryString, ean, attrSearchtext, productSearchtext;

    /*
        Java reflection is used and indicates possible bad code style, but ensures code flexibility.
        Reconsider this code snippet maybe.
     */

    /**
     * @return Returns a map containing the attribute name - value pairs of this class
     * @throws IllegalAccessException Gets thrown if any JavaReflection access failed.
     */
    @JsonIgnore
    public HashMap<String, String> getOfferSnapshot() throws IllegalAccessException {
        HashMap<String, String> snapshot = new HashMap<>();
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            snapshot.put(field.getName(), getValue(field.get(this)));
        }
        return snapshot;
    }

    /**
     * @param object The object that needs to be converted to a string representation.
     * @return String representation for the given object. If the object is a list or a map, the first value gets
     * returned.
     */
    @JsonIgnore
    private String getValue(Object object) {
        if (object instanceof Map) {
            Map map = (Map) object;
            if (!map.isEmpty()) { return map.values().iterator().next().toString(); }
        } else if (object instanceof List) {
            List list = (List) object;
            if (!list.isEmpty()) { return list.get(0).toString(); }
        } else if (object != null) {
            return object.toString();
        }
        return null;
    }
}
