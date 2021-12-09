package com.external.api.call.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.external.api.call.AutomationService;
import com.external.api.call.model.ResponseDataModel;

@Controller
public class AutomationController {

    @Autowired
    private AutomationService service;
    @GetMapping("/index")
    public String greeting(Model model) {
        model.addAttribute("datas", this.service.loadData());
        return "index";
    }
    @GetMapping("/viewdata")
    public String showApiDate(@PathVariable("cityName") String cityName, Model model){
    	model.addAttribute("apiDataList", this.service.getApiData(cityName));
        return "view";
   }
}
