package models;

import org.springframework.web.client.RestClient;

public class OpenAiImageEditModel implements ImageEditModel{

    private RestClient client;

    @Override
    public ImageEditResponse call(ImageEditPrompt request) {
        ;
    }
}
