package de.hpi.shoprulesgenerator.model;

import de.hpi.restclient.pojo.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
class ShopRulesGeneratorTest {

    private List<Offer> offers;
    private ShopRulesGenerator generator;

    @BeforeEach
    public void setUp() throws IOException {
        setOffers(JsonConverter.readJavaObjectFromInputStream(getClass().getClassLoader().getResourceAsStream
                ("local-offers.json"), LocalOffers.class).getOffers());
        for (Offer offer : getOffers()) {
            offer.getUrl().put("0", getClass().getClassLoader().getResource("offer-websites" + File.separator +
                    offer.getUrl().get("0")).toString());
        }
        setGenerator(new ShopRulesGenerator(10, 7, 100, 0));
    }

    @Test
    void generate() {
        Rules rules = getGenerator().generate(getOffers().iterator(), new FileHTMLFetcher(), new SelectorGenerator());
        for (Map.Entry<OfferAttribute, Rule> ruleEntry : rules.getRules().entrySet()){
            System.out.println(ruleEntry.getKey());
            for (RuleEntry entry : ruleEntry.getValue().getEntries()) {
                System.out.println(entry.getAttribute() + " " + entry.getSelector());
            }
            System.out.println("====================");
        }
    }
}