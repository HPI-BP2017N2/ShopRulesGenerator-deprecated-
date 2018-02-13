package de.hpi.shoprulesgenerator.repository;

import de.hpi.restclient.pojo.Rules;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rules")
@Getter @Setter
public class RulesDBEntry {

    private long shopID;
    private Rules rules;

    public RulesDBEntry(long shopID, Rules rules) {
        setShopID(shopID);
        setRules(rules);
    }
}
