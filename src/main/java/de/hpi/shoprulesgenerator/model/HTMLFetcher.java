package de.hpi.shoprulesgenerator.model;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

public interface HTMLFetcher {

    Document fetch(URL url) throws IOException;
}
