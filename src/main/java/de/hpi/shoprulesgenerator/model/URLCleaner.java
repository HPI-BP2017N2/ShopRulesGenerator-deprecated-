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

    private URLCleaner(URLCleanerClient client) {
        setClient(client);
    }

    public String clean(String dirtyUrl) throws Exception {
        CleanURLResponse response = getClient().cleanURL(dirtyUrl);
        if (response.isBlacklisted()) {
            throw new Exception("URL is blacklisted");
        }
        return response.getCleanUrl();
    }
}
