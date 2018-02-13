package de.hpi.shoprulesgenerator.model;

import de.hpi.restclient.clients.URLCleanerClient;
import de.hpi.restclient.dto.CleanURLResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

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
        return response.getCleanUrl();
    }
}
