package com.baseball.fractionassessment.service;

import com.baseball.fractionassessment.dto.BaseballPlayerDTO;
import com.baseball.fractionassessment.model.openai.OpenAiMsg;
import com.baseball.fractionassessment.model.openai.OpenAiRequest;
import com.baseball.fractionassessment.model.openai.OpenAiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.List;

@Service
@Slf4j
public class OpenAIService {

//    @Value("${OPEN_AI_API_KEY}")
//    private String apiKey;

    private final Environment environment;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final String url = "https://api.openai.com/v1/chat/completions";
    private final String defaultModel = "gpt-3.5-turbo-1106";

    @Autowired
    public OpenAIService(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper, Environment environment) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
        this.environment = environment;
    }

    public BaseballPlayerDTO generatePlayerDescription(BaseballPlayerDTO dto) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        String apiKey = environment.getProperty("OPEN_AI_API_KEY");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String prompt = MessageFormat.format("Provide a description and details about the life of the baseball " +
                "player {0} based on these stats: {1}",
                dto.getPlayer(), objectMapper.writeValueAsString(dto));


        OpenAiRequest request = OpenAiRequest.builder()
                .model(defaultModel)
                .messages(List.of(OpenAiMsg.builder().role("user").content(prompt).build()))
                .temperature(0.7)
                .build();
        HttpEntity<OpenAiRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<OpenAiResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                OpenAiResponse.class
        );
        log.info("retrievePlayerDescription: " + response.getBody());
        if (response.getStatusCode().is2xxSuccessful()) {
            OpenAiResponse responseBody = response.getBody();
            String description = responseBody.getChoices().get(0).getMessage().getContent();
            dto.setDescription(description);
        }

        return dto;
    }
}
