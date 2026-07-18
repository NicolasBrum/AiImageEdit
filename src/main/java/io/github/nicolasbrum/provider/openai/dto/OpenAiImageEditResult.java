package io.github.nicolasbrum.provider.openai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record OpenAiImageEditResult(
        Long created,
        @JsonProperty("output_format")
        String outputFormat,
        String quality,
        String size,
        List<OpenAiImageData> data
){}
