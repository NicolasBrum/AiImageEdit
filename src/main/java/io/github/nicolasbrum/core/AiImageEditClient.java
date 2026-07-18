package io.github.nicolasbrum.core;


public interface AiImageEditClient {

    AiImageEditResult send(AiImageEditPrompt prompt);
}
