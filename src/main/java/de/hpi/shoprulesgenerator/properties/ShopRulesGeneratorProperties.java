package de.hpi.shoprulesgenerator.properties;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Component
@EnableConfigurationProperties
@ConfigurationProperties("shoprulesgenerator")
@Getter @Setter
public class ShopRulesGeneratorProperties {

    @Min(1)
    @Max(20)
    private int pageSize;

    @Min(1)
    @Max(50)
    private int maxOfferCount;

    @Min(0)
    private int minMatchCountPerAttribute;

    @Min(0)
    @Max(100)
    private int minConfidence;

    @Min(0)
    private int fetchDelay;

    @NotBlank
    private String userAgent;
}
