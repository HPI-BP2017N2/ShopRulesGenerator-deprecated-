package de.hpi.shoprulesgenerator.service;

import de.hpi.shoprulesgenerator.dto.CleanResponse;
import de.hpi.shoprulesgenerator.dto.GetRandomOffersResponse;
import de.hpi.shoprulesgenerator.model.ShopRulesGenerator;
import de.hpi.shoprulesgenerator.model.data.Offer;
import de.hpi.shoprulesgenerator.model.data.Rule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
public class ShopRulesGeneratorService {

    @Getter(AccessLevel.PRIVATE) private static final int MAX_OFFER_COUNT = 10;
    @Getter(AccessLevel.PRIVATE) private static final String BP_BRIDGE_ROOT = "http://ts1552.byod.hpi.de:2162",
            RANDOM_OFFER_ROUTE = "/getRandomOffers", URL_CLEANER_ROOT = "", CLEAN_ROUTE = "/clean";

    private RestTemplate restTemplate;
    private ShopRulesGenerator generator;

    public ShopRulesGeneratorService(RestTemplateBuilder restTemplateBuilder) {
        setRestTemplate(restTemplateBuilder.build());
        setGenerator(new ShopRulesGenerator());
    }

    public HashMap<String, List<Rule>> getRulesForShop(long shopID) {
        URI targetUri = getURIForRandomOffers(shopID);
        List<Offer> randomOffers = getRestTemplate().getForObject(targetUri, GetRandomOffersResponse.class).getOffers();
        System.err.println("CLEANING IS DISABLED!");
        //        cleanOfferUrls(randomOffers);
        return getGenerator().getRulesForShop(randomOffers);
    }

    private void cleanOfferUrls(List<Offer> randomOffers) {
        List<String> dirtyUrls = new LinkedList<>();
        for (Offer offer : randomOffers) {
            dirtyUrls.add(offer.getUrl().get(Integer.toString(0)));
        }
        URI targetUri = getURIForURLCleaner(dirtyUrls);
        List<String> cleanedUrls = getRestTemplate().getForObject(targetUri, CleanResponse.class).getUrls();
        for (int iOffer = 0; iOffer < randomOffers.size(); iOffer++) {
            randomOffers.get(iOffer).getUrl().put(Integer.toString(0), cleanedUrls.get(iOffer));
        }
    }

    private URI getURIForRandomOffers(long shopID) {
        return UriComponentsBuilder.fromUriString(getBP_BRIDGE_ROOT())
                .path(getRANDOM_OFFER_ROUTE())
                .queryParam("shopID", shopID)
                .queryParam("count", getMAX_OFFER_COUNT())
                .build()
                .encode()
                .toUri();
    }

    private URI getURIForURLCleaner(List<String> urls) {
        return UriComponentsBuilder.fromUriString(getURL_CLEANER_ROOT())
                .path(getCLEAN_ROUTE())
                .queryParam("urls", urls)
                .build()
                .encode()
                .toUri();
    }
}
