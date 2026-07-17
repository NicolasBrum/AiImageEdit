package io.github.nicolasbrum.models;

public class AiImageEditModelFactory {

    public static AiImageEditModel getInstance(AiProvider aiModel) {
        return switch (aiModel) {
            case OPENAI -> new OpenAiAiImageEditModel(new OpenAiAiImageEditClient());
        };
    }
}
