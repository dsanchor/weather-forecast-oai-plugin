package com.microsoft.stu.weatherforecastoaiplugin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.stu.weatherforecastoaiplugin.service.SemanticKernel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.HashMap;
import java.util.Map;

@RestController
public class WeatherForecastController {

    @Autowired
    SemanticKernel semanticKernel;

    @Operation(summary = "Get weather forecast", description = "Get weather forecast for a given location")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Weather forecast retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/forecast")
    public Map<String, String> getWeatherForecast(@RequestParam String location) {
        System.out.println("Input location: " + location);
        
        String forecastSummary = semanticKernel.getWeatherForecastSummary(location);
                
        Map<String, String> result = new HashMap<>();
        result.put("forecast", forecastSummary);
        result.put("location", location);

        System.out.println("Output forecast summary: " + forecastSummary);
        return result;
    }
}