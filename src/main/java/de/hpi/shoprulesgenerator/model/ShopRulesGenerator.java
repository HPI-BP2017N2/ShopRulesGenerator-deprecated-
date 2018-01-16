package de.hpi.shoprulesgenerator.model;

import de.hpi.shoprulesgenerator.model.data.Offer;
import de.hpi.shoprulesgenerator.model.data.Rule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
public class ShopRulesGenerator {

    @Getter(AccessLevel.PRIVATE) private static final int HTTP_TIMEOUT = 5000;
    @Getter(AccessLevel.PRIVATE) private static final String USER_AGENT = "Mozilla/5.0 (compatible; " +
            "HPI-BPN2-2017/2.1; https://hpi.de/naumann/teaching/bachelorprojekte/inventory-management.html)";

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
        Document htmlDocument = loadHtml(new URL(offer.getUrl().get(Integer.toString(0))));
        if (htmlDocument != null) {
            for (Map.Entry<String, String> productAttribute : snapshot.entrySet()) {
                if (productAttribute.getValue() != null) {
                    selectors.put(productAttribute.getKey(), getXPathsForOfferAttribute(htmlDocument, productAttribute.getValue()));
                }
            }
        }
        return selectors;
    }

    private Document loadHtml(URL url) throws IOException {
        if (url.getProtocol().equals("file")){
            try {
                return Jsoup.parse(new File(url.toURI()), null);
            } catch (URISyntaxException ignored) {}
        } else if (url.getProtocol().startsWith("http")) {
            return Jsoup
                    .connect(url.toString())
                    .userAgent(getUSER_AGENT())
                    .get();
        }
        return null;
    }

    private List<String> getXPathsForOfferAttribute(Document html, String offerAttribute){
        offerAttribute = prepareHtml(offerAttribute);
        List<String> xPaths = new LinkedList<>();
        for (Element element : html.select("*:containsOwn(" + offerAttribute + ")")){
            xPaths.add(getXPathForDomElement(html, element));
        }
        return xPaths;
    }

    private String prepareHtml(String html) {
        return html
                .replace("\"", "\\\"")
                .replace("uml", "uml;");
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
