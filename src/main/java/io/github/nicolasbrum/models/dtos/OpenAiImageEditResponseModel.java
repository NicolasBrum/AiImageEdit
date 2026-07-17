package io.github.nicolasbrum.models.dtos;

import java.util.List;

public record OpenAiImageEditResponseModel(
        Long created,
        List<OpenAiImageData> data,
        String output_format,
        String quality,
        String size
) {
}
