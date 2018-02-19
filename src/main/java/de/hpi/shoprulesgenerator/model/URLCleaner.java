package de.hpi.shoprulesgenerator.model;

import de.hpi.restclient.clients.URLCleanerClient;
import de.hpi.restclient.dto.CleanURLResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class URLCleaner {

    private URLCleanerClient client;

    public URLCleaner(URLCleanerClient client) {
        setClient(client);
    }

    public String clean(String dirtyUrl, long shopID) throws Exception {
        CleanURLResponse response = getClient().cleanURL(dirtyUrl, shopID);
        if (response.isBlacklisted()) {
            throw new Exception("URL is blacklisted");
        }
        return decodeURL(response.getCleanUrl());
    }

    private String decodeURL(String cleanUrl) throws UnsupportedEncodingException {
        return URLDecoder.decode(cleanUrl, "UTF-8");
    }
}
