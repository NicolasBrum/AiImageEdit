package io.github.nicolasbrum.models;

import io.github.nicolasbrum.models.dtos.OpenAiImageEditResponseModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ReactorClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Component
public class OpenAiAiImageEditClient implements AiImageEditClient {

    private final static String BASE_URL = "https://api.openai.com/v1";
    private final RestClient restClient;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    public OpenAiAiImageEditClient(){
        var requestFactory = new ReactorClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(30));
        requestFactory.setReadTimeout(Duration.ofMinutes(3));

        this.restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .requestFactory(requestFactory)
                .build();
    }

    @Override
    public OpenAiImageEditResponseModel send(AiImageEditPrompt aiImageEditPrompt) {

        var instructions = aiImageEditPrompt.getInstructions();
        var resource = aiImageEditPrompt.getResource();
        var options = (ImageOptions) aiImageEditPrompt.getOptions();
        var mediaType = aiImageEditPrompt.getMediaType();

        HttpHeaders imageHeaders = new HttpHeaders();
        imageHeaders.setContentType(mediaType);

        HttpEntity<Resource> imagePart = new HttpEntity<>(resource, imageHeaders);
        MultiValueMap<String, Object> multipartBody = new LinkedMultiValueMap<>();
        multipartBody.add("prompt", String.join("\n", instructions));
        multipartBody.add("model", options.getModel());
        multipartBody.add("n", options.getN().toString());
        multipartBody.add("image", imagePart);

        var response = restClient.post()
                .uri("/images/edits")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(multipartBody)
                .retrieve()
                .toEntity(OpenAiImageEditResponseModel.class);

        return response.getBody();
    }
}
