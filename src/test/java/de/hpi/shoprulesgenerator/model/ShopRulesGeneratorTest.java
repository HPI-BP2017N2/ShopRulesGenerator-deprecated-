package de.hpi.shoprulesgenerator.model;

import de.hpi.shoprulesgenerator.model.data.Offer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
class ShopRulesGeneratorTest {

    private List<Offer> offers;
    private ShopRulesGenerator shopRulesGenerator;

    @BeforeEach
    public void setUp() throws IOException {
        setOffers(JsonConverter.readJavaObjectFromInputStream(getClass().getClassLoader().getResourceAsStream
                ("local-offers.json"), LocalOffers.class).getOffers());
        for (Offer offer : getOffers()) {
            offer.getUrl().put("0", getClass().getClassLoader().getResource("offer-websites" + File.separator +
                    offer.getUrl().get("0")).toString());
        }
        setShopRulesGenerator(new ShopRulesGenerator());
    }

    @Test
    void getRulesForShop() {
        getShopRulesGenerator().getRulesForShop(getOffers());
    }
}