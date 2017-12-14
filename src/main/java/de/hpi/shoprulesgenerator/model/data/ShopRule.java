package de.hpi.shoprulesgenerator.model.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shopRules")
@Getter @Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ShopRule {

    private long timestamp;
    private String shopRootUrl, shopRule;

}
