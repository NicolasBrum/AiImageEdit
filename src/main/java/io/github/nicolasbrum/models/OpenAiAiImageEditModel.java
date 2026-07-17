package io.github.nicolasbrum.models;

import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.image.ImageResponseMetadata;
import java.util.List;

public class OpenAiAiImageEditModel implements AiImageEditModel {

    private AiImageEditClient client;

    public OpenAiAiImageEditModel(AiImageEditClient client){
        this.client = client;
    }

    @Override
    public ImageResponse call(AiImageEditPrompt prompt) {
        var response = client.send(prompt);

        ImageResponseMetadata imageMetadata = new ImageResponseMetadata(response.created());

        List<ImageGeneration> generations = response.data()
                .stream()
                .map(generation -> new ImageGeneration(new Image(null, generation.base64image())))
                .toList();

        return new ImageResponse(generations, imageMetadata);
    }
}
