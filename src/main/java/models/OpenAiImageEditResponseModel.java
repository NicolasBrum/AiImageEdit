package models;

public record OpenAiImageEditResponseModel(
        Long created,
        OpenAiImageData data
) {
}
