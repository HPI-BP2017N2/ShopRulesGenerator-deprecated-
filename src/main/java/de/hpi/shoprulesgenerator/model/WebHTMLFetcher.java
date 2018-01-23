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

    public WebHTMLFetcher(String userAgent) {
        setUserAgent(userAgent);
    }

    @Override
    public Document fetch(URL url) throws IOException {
        return Jsoup
                .connect(url.toString())
                .userAgent(getUserAgent())
                .get();
    }
}
