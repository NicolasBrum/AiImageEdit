package io.github.nicolasbrum.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OpenAiImageData(
        @JsonProperty("b64_json")
        String base64image
) {
}
