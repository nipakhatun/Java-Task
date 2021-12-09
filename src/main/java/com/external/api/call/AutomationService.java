package com.external.api.call;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.external.api.call.controller.AutomationExecutor;
import com.external.api.call.controller.DataSource;
import com.external.api.call.controller.RestCaller;
import com.external.api.call.model.DataSourceModel;
import com.external.api.call.model.ResponseDataModel;
import com.external.api.call.model.responses.HotelResponse;
import com.external.api.call.model.responses.WeatherResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class AutomationService {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private  RestCaller restCaller;
    

    public List<DataSourceModel> loadData(){
         dataSource.loadData();
         return dataSource.getCountryList();
    }
    
    public  List<ResponseDataModel> getApiData(String cityName) {
        List<DataSourceModel> dataSourceModelList = dataSource.getCountryList();
        if (dataSourceModelList.isEmpty()) {
            dataSource.loadData();
        }
        List<ResponseDataModel> models = new ArrayList<>();
        for (DataSourceModel dataSourceModel : dataSourceModelList) {
            HotelResponse hotelResponse = restCaller.getHotel(cityName);
            WeatherResponse weatherResponse = restCaller.getWeather(cityName);
            models.add(new ResponseDataModel(dataSourceModel.getCountryName(), hotelResponse, weatherResponse));
        }
        return models;
    }
    
}
