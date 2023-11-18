package com.microsoft.stu.weatherforecastoaiplugin.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class OAIPluginManifestController {
    

    @Value("${oai.plugin.baseurl}")
    private String baseurl;
    

    @Operation(summary = "This method is used to get the ChatGTP plugin spec.", hidden = true)
    @GetMapping("/.well-known/ai-plugin.json")
    public ResponseEntity<String> getAiPluginConfig() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/ai-plugin.json");
        String content = new String(resource.getInputStream().readAllBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(content.replace("${baseurl}", baseurl), headers, HttpStatus.OK);
    }

}
