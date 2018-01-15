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