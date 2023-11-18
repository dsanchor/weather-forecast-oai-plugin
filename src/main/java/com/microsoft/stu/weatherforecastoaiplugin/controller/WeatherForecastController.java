package com.microsoft.stu.weatherforecastoaiplugin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.HashMap;
import java.util.Map;

@RestController
public class WeatherForecastController {

    @Operation(summary = "Get weather forecast", description = "Get weather forecast for a given location")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Weather forecast retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/forecast")
    public Map<String, String> getWeatherForecast(@RequestParam String location) {
        Map<String, String> forecast = new HashMap<>();
        forecast.put("location", location);
        forecast.put("forecast", "Sunny with a chance of rain"); // Dummy forecast
        return forecast;
    }
}