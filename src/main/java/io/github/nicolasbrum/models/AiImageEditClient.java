package io.github.nicolasbrum.models;

import io.github.nicolasbrum.models.dtos.OpenAiImageEditResponseModel;


public interface AiImageEditClient {

    OpenAiImageEditResponseModel send(AiImageEditPrompt prompt);
}
