package de.hpi.shoprulesgenerator.service;

import de.hpi.restclient.clients.BPBridgeClient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
public class ShopRulesGeneratorService {

    private BPBridgeClient bpBridgeClient;

    @Autowired
    public ShopRulesGeneratorService(BPBridgeClient bpBridgeClient) {
        setBpBridgeClient(bpBridgeClient);
    }

}
