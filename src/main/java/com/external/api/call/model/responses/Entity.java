
package com.external.api.call.model.responses;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Entity {

    private String geoId;
    private String destinationId;
    private String landmarkCityDestinationId;
    private String type;
    private String redirectPage;
    private Double latitude;
    private Double longitude;
    private String searchDetail;
    private String caption;
    private String name;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
