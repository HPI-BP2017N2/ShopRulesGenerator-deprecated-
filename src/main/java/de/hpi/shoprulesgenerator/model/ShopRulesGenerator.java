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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
public class ShopRulesGenerator {

    @Getter(AccessLevel.PRIVATE) private static final int HTTP_TIMEOUT = 5000;
    @Getter(AccessLevel.PRIVATE) private static final String USER_AGENT = "Mozilla/5.0 (compatible; " +
            "HPI-BPN2-2017/2.1; https://hpi.de/naumann/teaching/bachelorprojekte/inventory-management.html)";

    public HashMap<String, List<Rule>> getRulesForShop(List<Offer> offers) {
        HashMap<String, List<Rule>> rules = new HashMap<>();
        HashMap<String, HashMap<String, Integer>> selectorsMap = new HashMap<>();
        for (Offer offer : offers) {
            try {
                HashMap<String, List<String>> selectors = getSelectorsForOffer(offer);
                for (Map.Entry<String, List<String>> entry : selectors.entrySet()) {
                    if (!selectorsMap.containsKey(entry.getKey())) {
                        selectorsMap.put(entry.getKey(), new HashMap<String, Integer>());
                    }
                    for (String selector : entry.getValue()) {
                        if (selectorsMap.get(entry.getKey()).containsKey(selector)) {
                            selectorsMap.get(entry.getKey()).put(selector, selectorsMap.get(entry.getKey()).get
                                    (selector) + 1);
                        } else {
                            selectorsMap.get(entry.getKey()).put(selector, 1);
                        }
                    }
                }
            } catch (IllegalAccessException | IOException ignored) {}
        }
        for (Map.Entry<String, HashMap<String, Integer>> entry : selectorsMap.entrySet()) {
            for (Iterator<Map.Entry<String, Integer>> iterator = entry.getValue().entrySet().iterator(); iterator
                    .hasNext();) {
                Map.Entry<String, Integer> selectorEntry = iterator.next();
                if (selectorEntry.getValue() == 10) { //this need to be changed, if attribute was not found
                    if (!rules.containsKey(entry.getKey())) {
                        rules.put(entry.getKey(), new LinkedList<Rule>());
                    }
                    Rule rule = new Rule();
                    rule.setAttribute(null);
                    rule.setResultAsPlainText(true);
                    rule.setXPath(selectorEntry.getKey());
                    rules.get(entry.getKey()).add(rule);
                }
            }
        }
        for (Map.Entry<String, List<Rule>> entry : rules.entrySet()) {
            System.out.println("================");
            System.out.println(entry.getKey());
            System.out.println("================");
            for (Rule rule : entry.getValue()) {
                System.out.println(rule.getXPath());
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
