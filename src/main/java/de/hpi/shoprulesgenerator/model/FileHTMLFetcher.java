package de.hpi.shoprulesgenerator.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class FileHTMLFetcher implements HTMLFetcher {

    @Override
    public Document fetch(URL url, long shopID) throws IOException, URISyntaxException {
        if (!url.getProtocol().equals("file")) {
            throw new IOException("Protocol of url is not a valid file protocol");
        }
        return Jsoup.parse(new File(url.toURI()), null);
    }
}
