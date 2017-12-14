package de.hpi.shoprulesgenerator.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class GenerateResponse {

    private String shopRulesJson;
}
