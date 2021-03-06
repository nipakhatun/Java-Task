package com.external.api.call.controller;

import com.external.api.call.model.*;
import com.external.api.call.model.responses.Entity;
import com.external.api.call.model.responses.HotelResponse;
import com.external.api.call.model.responses.WeatherResponse;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AutomationWorker {
    final static Logger logger = LoggerFactory.getLogger(AutomationExecutor.class);

    private final RestCaller restCaller;
    private final DataSource dataSource;

    public List<CountryDataFormat> startProcess() {
        final List<ResponseDataModel> responseDataModels = loadData();
        return formatData(responseDataModels);
    }


    private synchronized List<ResponseDataModel> loadData(DataSourceModel... dataSourceModels) {
        List<DataSourceModel> dataSourceModelList;
        if (dataSourceModels == null || dataSourceModels.length == 0) {
            dataSourceModelList = dataSource.getCountryList();
        } else {
            dataSourceModelList = Arrays.asList(dataSourceModels);
        }

        if (dataSourceModelList.isEmpty()) {
            dataSource.loadData();
        }
        List<ResponseDataModel> models = new ArrayList<>();
        for (DataSourceModel dataSourceModel : dataSourceModelList) {
            HotelResponse hotelResponse = restCaller.getHotel(dataSourceModel.getCityName());
            WeatherResponse weatherResponse = restCaller.getWeather(dataSourceModel.getCityName());
            models.add(new ResponseDataModel(dataSourceModel.getCountryName(), hotelResponse, weatherResponse));
        }
        return models;
    }

    private synchronized List<CountryDataFormat> formatData(List<ResponseDataModel> models) {
        return models
                .stream()
                .filter(e -> e.getHotelResponse() != null)
                .map(data -> {
                    HotelResponse hotelResponse = data.getHotelResponse();
                    WeatherResponse weatherResponse = data.getWeatherResponse();
                    CountryDataFormat countryDataFormat = new CountryDataFormat();
                    countryDataFormat.setCountryName(data.getCountryName());
                    countryDataFormat.setCityName(hotelResponse.getTerm());
                    List<Hotel> hotels = getHotels(hotelResponse);
                    countryDataFormat.setHotelList(hotels);
                    Weather weather = generateWeather(weatherResponse);
                    countryDataFormat.setWeather(weather);
                    return countryDataFormat;
                })
                .collect(Collectors.toList());
    }

    private Weather generateWeather(WeatherResponse weatherResponse) {
        if (weatherResponse == null) {
            return null;
        }
        Weather weather = new Weather();
        weather.setDetails(weatherResponse.getMain());
        weather.setSunrise(weatherResponse.getSys().getSunrise());
        weather.setSunset(weatherResponse.getSys().getSunset());
        weather.setTimeZone(weatherResponse.getTimezone());
        return weather;
    }

    private List<Hotel> getHotels(HotelResponse hotelResponse) {
        return hotelResponse.getSuggestions().stream()
                .map(data -> {
                    Hotel hotel = new Hotel();
                    List<Entity> entities = data.getEntities();
                    if (entities.isEmpty()) {
                        return null;
                    }
                    for (Entity entity : entities) {
                        hotel.setName(entity.getName());
                        hotel.setName(entity.getCaption());
                        hotel.setCompanyName(data.getGroup());
                    }
                    return hotel;
                }).collect(Collectors.toList());
    }

    public List<CountryDataFormat> loadDataFromUI(DataSourceModel dataSourceModel) {
        final List<ResponseDataModel> responseDataModels = loadData(dataSourceModel);
        return formatData(responseDataModels);
    }

}
