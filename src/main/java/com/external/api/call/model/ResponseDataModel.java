package com.external.api.call.model;


import com.external.api.call.model.responses.HotelResponse;
import com.external.api.call.model.responses.WeatherResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseDataModel {
    private String countryName;
    private HotelResponse hotelResponse;
    private WeatherResponse weatherResponse;
}
