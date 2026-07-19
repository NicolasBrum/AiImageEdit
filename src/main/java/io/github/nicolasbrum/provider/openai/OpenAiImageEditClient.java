package io.github.nicolasbrum.provider.openai;

import io.github.nicolasbrum.config.AiProvidersApiUrl;
import io.github.nicolasbrum.core.AiImageEditClient;
import io.github.nicolasbrum.core.AiImageEditPrompt;
import io.github.nicolasbrum.core.AiImageEditResult;
import io.github.nicolasbrum.provider.openai.dto.OpenAiImageEditResult;
import org.springframework.ai.image.ImageOptions;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

public class OpenAiImageEditClient implements AiImageEditClient {

    private final RestClient restClient;

    public OpenAiImageEditClient(String apikey, Duration connectionTimeout, Duration readTimeout) {
        var httpClient = HttpClient.newBuilder()
                .connectTimeout(connectionTimeout)
                .build();
        var requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(readTimeout);

        this.restClient = RestClient.builder()
                .baseUrl(AiProvidersApiUrl.OPENAI)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apikey)
                .requestFactory(requestFactory)
                .build();
    }

    @Override
    public AiImageEditResult send(AiImageEditPrompt aiImageEditPrompt) {

        var instructions = aiImageEditPrompt.getInstructions();
        var resource = aiImageEditPrompt.getResource();
        var options = (ImageOptions) aiImageEditPrompt.getOptions();
        var mediaType = aiImageEditPrompt.getMediaType();

        HttpHeaders imageHeaders = new HttpHeaders();
        imageHeaders.setContentType(mediaType);

        HttpEntity<Resource> imagePart = new HttpEntity<>(resource, imageHeaders);
        MultiValueMap<String, Object> multipartBody = new LinkedMultiValueMap<>();
        multipartBody.add("prompt", String.join("\n", instructions));
        multipartBody.add("image", imagePart);

        multipartBody.add("model", options.getModel() == null ? "gpt-image-1" : options.getModel());
        multipartBody.add("n", options.getN() == null ? 1 : options.getN());

        OpenAiImageEditResult openAiResult = restClient.post()
                .uri("/images/edits")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(multipartBody)
                .retrieve()
                .toEntity(OpenAiImageEditResult.class)
                .getBody();

        return new OpenAiImageResultAdapter(openAiResult);
    }
}
