package de.hpi.shoprulesgenerator.dto;

import de.hpi.shoprulesgenerator.model.data.Rule;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter @Setter
public class GetResponse {

    private HashMap<String, List<Rule>> rules;
}
