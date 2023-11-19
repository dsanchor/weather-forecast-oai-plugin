package com.microsoft.stu.weatherforecastoaiplugin.plugins;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.semantickernel.skilldefinition.annotations.DefineSKFunction;
import com.microsoft.semantickernel.skilldefinition.annotations.SKFunctionInputAttribute;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Forecast {

    @DefineSKFunction(name = "GetForecastJsonData", 
        description = "Get weather forecast as Json for a given set of GPS coordinates",
        returnType = "string",
        returnDescription = "Weather forecast for the given coordinates in json format")
    public String getForecastJsonData(@SKFunctionInputAttribute(description = "set of GPS coordinates") String jsonCoordinates) {
        System.out.println("Input json coordinates: " + jsonCoordinates);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(jsonCoordinates);
        } catch (Exception e) {
            System.out.println("Error parsing json coordinates: " + e.getMessage());
            return "{ \"error\": \"Error parsing json coordinates\" }";
        }

        String latitude = jsonNode.get("latitude").asText();
        String longitude = jsonNode.get("longitude").asText();
        String city = jsonNode.get("city").asText();        

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.open-meteo.com/v1/forecast?latitude={latitude}&longitude={longitude}&daily=temperature_2m_max,temperature_2m_min,precipitation_sum,rain_sum,showers_sum,snowfall_sum,precipitation_hours,precipitation_probability_mean,wind_speed_10m_max";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        String response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class, latitude, longitude).getBody();        
       
        ObjectMapper mapperForResponse = new ObjectMapper();
        JsonNode jsonNodeForResponse = null;
        try {
                jsonNodeForResponse = mapperForResponse.readTree(response);
                ((ObjectNode) jsonNodeForResponse).put("city", city);
        } catch (Exception e) {
            System.out.println("Error parsing response: " + e.getMessage());
            return "{ \"error\": \"Error parsing response\" }";
        }

        System.out.println("Output forecast json: " + jsonNodeForResponse.toPrettyString());
        return jsonNodeForResponse.toPrettyString();
    }    
    
}
