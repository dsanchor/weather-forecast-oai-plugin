package com.microsoft.stu.weatherforecastoaiplugin.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.SKBuilders;
import com.microsoft.semantickernel.connectors.ai.openai.util.AzureOpenAISettings;
import com.microsoft.semantickernel.exceptions.ConfigurationException;
import com.microsoft.semantickernel.orchestration.ContextVariables;
import com.microsoft.semantickernel.orchestration.SKContext;
import com.microsoft.semantickernel.planner.actionplanner.Plan;
import com.microsoft.semantickernel.planner.sequentialplanner.SequentialPlanner;
import com.microsoft.semantickernel.skilldefinition.ReadOnlyFunctionCollection;
import com.microsoft.semantickernel.skilldefinition.ReadOnlySkillCollection;
import com.microsoft.stu.weatherforecastoaiplugin.plugins.Forecast;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;

@Service
public class SemanticKernel {

    @Value("${client.azureopenai.key}")
    private String azOaiKey;

    @Value("${client.azureopenai.endpoint}")
    private String azOaiEndpoint;

    @Value("${client.azureopenai.deploymentname}")
    private String azOaiDeployment;


    private Kernel kernel;

    @PostConstruct
    public void init() {
        System.out.println("SemanticKernel initialization");
        try {
            kernel = kernel();
        } catch (ConfigurationException e) {
            System.out.println("SemanticKernel initialization failed");
            e.printStackTrace();
        }
    }

    public String getWeatherForecastSummary(String location) {

        System.out.println("=> Run the Kernel");
        
        ContextVariables variables = SKBuilders.variables()
                .withInput(location)
                .build();
                
        Mono<SKContext> result =
        getKernel().runAsync(
                variables,
                getKernel().getSkill("Locator").getFunction("CityCoordinates", null),
                getKernel().getSkill("Forecast").getFunction("GetForecastJsonData", null),
                getKernel().getSkill("Weather").getFunction("ForecastSummarizer", null)
                );

        System.out.println("=> Result");
        String summary = result.block().getResult();
        System.out.println(summary);

        return summary;
    }

    /**
    public String plan(String goal) {
        // create a planner
        SequentialPlanner planner = new SequentialPlanner(getKernel(), null, null);
        // create a plan
        Plan plan = planner.createPlanAsync(goal).block();
        System.out.println("=> Plan <=");
        System.out.println(plan.toPlanString());
        // execute the plan
        String result = plan.invokeAsync().block().getResult();
        System.out.println("=> Result <=");
        System.out.println(result);
        return result;
    }
    */

    private Kernel kernel() throws ConfigurationException {

        OpenAIAsyncClient client = openAIAsyncClient();

        Kernel kernel = SKBuilders.kernel()
                .withDefaultAIService(SKBuilders.chatCompletion()
                        .withModelId(settings().getDeploymentName())
                        .withOpenAIClient(client)
                        .build())
                .build();

        kernel.importSkillFromResources("plugins", "Locator", "CityCoordinates");
        kernel.importSkillFromResources("plugins", "Weather", "ForecastSummarizer");
        // kernel.importSkillFromDirectory("Locator", "src/main/resources/plugins", "Locator");
        // kernel.importSkillFromDirectory("Weather", "src/main/resources/plugins", "Weather");
        kernel.importSkill(new Forecast(), "Forecast");

        ReadOnlySkillCollection collection =  kernel.getSkills();
        ReadOnlyFunctionCollection functions = collection.getAllFunctions();
        System.out.println("=> Registered functions");
        functions.getAll().forEach(function -> {
            System.out.println(function.getSkillName()+"."+function.getName());
        });
        return kernel;
    }

    private OpenAIAsyncClient openAIAsyncClient() throws ConfigurationException {
        // Create an Azure OpenAI client
        AzureOpenAISettings settings = settings();
        return new OpenAIClientBuilder().endpoint(settings.getEndpoint()).credential(new AzureKeyCredential(settings.getKey())).buildAsyncClient();
    }

    private AzureOpenAISettings settings() throws ConfigurationException {

        Map<String, String> map = new HashMap<>();
        map.put(AzureOpenAISettings.getDefaultSettingsPrefix()+"."
            + AzureOpenAISettings.getKeySuffix(), azOaiKey);
        map.put(AzureOpenAISettings.getDefaultSettingsPrefix()+"."
            + AzureOpenAISettings.getAzureOpenAiEndpointSuffix(), azOaiEndpoint);
        map.put(AzureOpenAISettings.getDefaultSettingsPrefix()+"."
            + AzureOpenAISettings.getAzureOpenAiDeploymentNameSuffix(), azOaiDeployment);

        AzureOpenAISettings settings = new AzureOpenAISettings(map);
        System.out.println("=> Azure OpenAI Settings");
        System.out.println("Key: " + (Strings.isEmpty(settings.getKey()) ? "" : "****"));
        System.out.println("Endpoint: " + settings.getEndpoint());
        System.out.println("Deployment Name: " + settings.getDeploymentName());
        return settings;
    }


    private Kernel getKernel() {
        return kernel;
    }
}
