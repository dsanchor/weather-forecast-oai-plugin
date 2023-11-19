package com.microsoft.stu.weatherforecastoaiplugin.plugins;

import com.microsoft.semantickernel.skilldefinition.annotations.DefineSKFunction;
import com.microsoft.semantickernel.skilldefinition.annotations.SKFunctionInputAttribute;

public class Forecast {

    @DefineSKFunction(name = "GetForecastJsonData", 
        description = "Get weather forecast as Json for a given set of GPS coordinates",
        returnType = "string",
        returnDescription = "Weather forecast for the given coordinates in json format")
    public String getForecastJsonData(@SKFunctionInputAttribute(description = "set of GPS coordinates") String jsonCoordinates) {
        System.out.println("Input json coordinates: " + jsonCoordinates);
        return "{\"latitude\":59.3289,\"longitude\":18.072342,\"generationtime_ms\":0.18405914306640625,\"utc_offset_seconds\":0,\"timezone\":\"GMT\",\"timezone_abbreviation\":\"GMT\",\"elevation\":24.0,\"daily_units\":{\"time\":\"iso8601\",\"temperature_2m_max\":\"°C\",\"temperature_2m_min\":\"°C\",\"precipitation_sum\":\"mm\",\"rain_sum\":\"mm\",\"showers_sum\":\"mm\",\"snowfall_sum\":\"cm\",\"precipitation_hours\":\"h\",\"precipitation_probability_mean\":\"%\",\"wind_speed_10m_max\":\"km/h\"},\"daily\":{\"time\":[\"2023-11-19\",\"2023-11-20\",\"2023-11-21\",\"2023-11-22\",\"2023-11-23\",\"2023-11-24\",\"2023-11-25\"],\"temperature_2m_max\":[1.8,3.1,1.4,2.0,2.4,-0.2,-6.2],\"temperature_2m_min\":[-3.7,-0.0,-3.8,-5.5,-0.0,-6.2,-8.2],\"precipitation_sum\":[1.50,0.00,0.00,11.00,0.40,0.00,0.00],\"rain_sum\":[1.50,0.00,0.00,0.00,0.00,0.00,0.00],\"showers_sum\":[0.00,0.00,0.00,0.00,0.00,0.00,0.00],\"snowfall_sum\":[0.00,0.00,0.00,5.67,0.00,0.00,0.00],\"precipitation_hours\":[5.0,0.0,0.0,11.0,4.0,0.0,0.0],\"precipitation_probability_mean\":[6,26,29,84,77,23,35],\"wind_speed_10m_max\":[11.9,10.1,13.3,22.1,13.3,17.3,17.0]}}";
    }
    
}
