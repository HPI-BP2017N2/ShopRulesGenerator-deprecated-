package de.hpi.shoprulesgenerator.model;

import de.hpi.restclient.pojo.Offer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class LocalOffers {

    private List<Offer> offers;
}
