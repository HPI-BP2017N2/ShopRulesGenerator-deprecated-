package de.hpi.shoprulesgenerator.model;

import de.hpi.shoprulesgenerator.model.data.Offer;
import de.hpi.shoprulesgenerator.model.data.Rule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
public class ShopRulesGenerator {

    @Getter(AccessLevel.PRIVATE) private static final int HTTP_TIMEOUT = 5000;

    public HashMap<String, List<Rule>> getRulesForShop(List<Offer> offers) {
        for (Offer offer : offers) {
            try {
                System.out.println(getSelectorsForOffer(offer));
            } catch (IllegalAccessException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private HashMap<String, List<String>> getSelectorsForOffer(Offer offer) throws IllegalAccessException, IOException {
        HashMap<String, List<String>> selectors = new HashMap<>();
        HashMap<String, String> snapshot = offer.getOfferSnapshot();
        Document htmlDocument = Jsoup.parse(new URL(offer.getUrl().get(Integer.toString(0))), getHTTP_TIMEOUT());
        for (Map.Entry<String, String> productAttribute : snapshot.entrySet()) {
            if (productAttribute.getValue() != null) {
                selectors.put(productAttribute.getKey(), getXPathsForOfferAttribute(htmlDocument, productAttribute.getValue()));
            }
        }
        return selectors;
    }

    private List<String> getXPathsForOfferAttribute(Document html, String offerAttribute){
        /*
        TODO NEXT
        escape attributes like " &aum
         */
        List<String> xPaths = new LinkedList<>();
        System.out.println("*:containsOwn(" + offerAttribute + ")");
        for (Element element : html.select("*:containsOwn(" + offerAttribute + ")")){
            xPaths.add(getXPathForDomElement(html, element));
        }
        return xPaths;
    }

    private String getXPathForDomElement(Document html, Element element){
        StringBuilder xPathBuilder = new StringBuilder();
        while (!isElementIDSet(element) && element.parent() != null){
            int tagIndex = getTagIndexForChild(element.parent(), element);
            xPathBuilder.insert(0, " " + element.tagName() + ":eq(" + tagIndex + ")");
            element = element.parent();
        }
        xPathBuilder.insert(0,"*#" + element.id());
        return xPathBuilder.toString();
    }

    private int getTagIndexForChild(Element parent, Element child){
        Elements childElementsWithTag = parent.getElementsByTag(child.tagName());
        for (int iElement = 0; iElement < childElementsWithTag.size(); iElement++){
            if (childElementsWithTag.get(iElement).equals(child)){
                return iElement;
            }
        }
        return -1;
    }

    private boolean isElementIDSet(Element element) {
        return !element.id().isEmpty();
    }

}
