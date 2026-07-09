package models;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;


public class OpenAiImageEditClient {

    private final String BASE_URL = "https://api.openai.com/v1";
    private final RestClient restClient;

    public OpenAiImageEditClient(String apiKey){
        this.restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }

    public void send(ByteArrayResource fileBytes) {

        HttpHeaders filePartHeaders = new HttpHeaders();
        filePartHeaders.setContentType(MediaType.IMAGE_JPEG);
        filePartHeaders.setContentDisposition(ContentDisposition.formData()
                .name("image")
                .filename(fileBytes.getFilename())
                .build());

        HttpEntity<ByteArrayResource> entity = new HttpEntity<>(fileBytes,filePartHeaders);

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("prompt","teste");
        formData.add("model","gpt-image-1");
        formData.add("image", entity);

        var r = restClient.post()
                .uri("/images/edits")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(formData)
                .retrieve();

    }
}
