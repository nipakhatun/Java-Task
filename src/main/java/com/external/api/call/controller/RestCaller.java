package com.external.api.call.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.external.api.call.model.responses.HotelResponse;
import com.external.api.call.model.responses.WeatherResponse;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class RestCaller {
    private final static Logger logger = LoggerFactory.getLogger(RestCaller.class);

    private final RestTemplate restTemplate;

    @Value("${hotel.api.key}")
    private String hotelApiKey;
    @Value("${hotel.url}")
    private String hotelApiUrl;
    @Value("${weather.api.key}")
    private String weatherApiKey;
    @Value("${weather.url}")
    private String weatherApiUrl;
   


    public synchronized HotelResponse getHotel(String cityName) {
        try {
            String url = hotelApiUrl + cityName;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("x-rapidapi-host", "hotels4.p.rapidapi.com");
            httpHeaders.add("x-rapidapi-key", hotelApiKey);
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

            ResponseEntity<HotelResponse> responseEntity =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            httpEntity,
                            HotelResponse.class
                    );

            return responseEntity.getBody();
        }
        catch (RestClientException e) {
            logger.error("can't fetch hotel now due to {}", e.getMessage());
        }
        return null;
    }

    public synchronized WeatherResponse getWeather(String cityName) {
        try {
            String url = weatherApiUrl + cityName + "&appid=" + weatherApiKey;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

            ResponseEntity<WeatherResponse> responseEntity =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            httpEntity,
                            WeatherResponse.class
                    );

            return responseEntity.getBody();
        }
        catch (Exception e) {
            logger.error("can't fetch weather now due to {}", e.getMessage());
            return null;
        }
    }


}
