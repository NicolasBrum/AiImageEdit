package io.github.nicolasbrum.core;

import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImageResponse;
import java.util.List;

public class DefaultAiImageEditModel implements AiImageEditModel {

    private final AiImageEditClient client;

    public DefaultAiImageEditModel(AiImageEditClient client){
        this.client = client;
    }

    @Override
    public ImageResponse call(AiImageEditPrompt prompt) {
        var response = client.send(prompt);
        var imageMetadata = response.metadata();

        List<ImageGeneration> generations = response.images()
                .stream()
                .map(ImageGeneration::new)
                .toList();

        return new ImageResponse(generations, imageMetadata);
    }
}
