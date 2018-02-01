package de.hpi.shoprulesgenerator.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class WebHTMLFetcher implements HTMLFetcher {

    private String userAgent;
    private URLCleaner urlCleaner;

    public WebHTMLFetcher(URLCleaner urlCleaner, String userAgent) {
        setUrlCleaner(urlCleaner);
        setUserAgent(userAgent);
    }

    @Override
    public Document fetch(URL url) throws Exception {
        URL cleanedURL = cleanURL(url);
        return Jsoup
                .connect(cleanedURL.toString())
                .userAgent(getUserAgent())
                .get();
    }

    private URL cleanURL(URL url) throws Exception {
        String dirtyUrl = url.toString();
        String cleanUrl = getUrlCleaner().clean(dirtyUrl);
        return new URL(cleanUrl);
    }
}
