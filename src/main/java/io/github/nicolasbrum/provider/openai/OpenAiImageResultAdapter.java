package io.github.nicolasbrum.provider.openai;


import io.github.nicolasbrum.config.AiImageEditMetadataKeys;
import io.github.nicolasbrum.core.AiImageEditResult;
import io.github.nicolasbrum.provider.openai.dto.OpenAiImageEditResult;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageResponseMetadata;

import java.util.List;
import java.util.Objects;

public class OpenAiImageResultAdapter implements AiImageEditResult {

    private final List<Image> images;
    private final ImageResponseMetadata imageResponseMetadata;

    public OpenAiImageResultAdapter(OpenAiImageEditResult result) {
        Objects.requireNonNull(result, "result must not be null");
        var data = Objects.requireNonNull(result.data(), "result.data must not be null");

        this.images = data
                .stream()
                .map(imageData -> {
                    Objects.requireNonNull(imageData, "image data must not be null");
                    var base64Image = Objects.requireNonNull(
                            imageData.base64image(),
                            "image data base64 must not be null"
                    );
                    return new Image(null, base64Image);
                })
                .toList();

        this.imageResponseMetadata = new ImageResponseMetadata(result.created());
        putMetadataIfPresent(AiImageEditMetadataKeys.QUALITY, result.quality());
        putMetadataIfPresent(AiImageEditMetadataKeys.OUTPUT_FORMAT, result.outputFormat());
        putMetadataIfPresent(AiImageEditMetadataKeys.SIZE, result.size());
    }

    private void putMetadataIfPresent(String key, Object value) {
        if (value != null) {
            this.imageResponseMetadata.put(key, value);
        }
    }

    @Override
    public List<Image> images() {
        return this.images;
    }

    @Override
    public ImageResponseMetadata metadata() {
        return this.imageResponseMetadata;
    }
}
