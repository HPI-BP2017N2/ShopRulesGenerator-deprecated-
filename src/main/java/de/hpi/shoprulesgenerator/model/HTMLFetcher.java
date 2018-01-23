package de.hpi.shoprulesgenerator.model;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public interface HTMLFetcher {

    Document fetch(URL url) throws IOException, URISyntaxException;
}
