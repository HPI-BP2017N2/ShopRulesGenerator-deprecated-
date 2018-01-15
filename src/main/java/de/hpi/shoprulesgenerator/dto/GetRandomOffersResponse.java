package de.hpi.shoprulesgenerator.dto;

import de.hpi.shoprulesgenerator.model.data.Offer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class GetRandomOffersResponse {

    private List<Offer> offers;

}
