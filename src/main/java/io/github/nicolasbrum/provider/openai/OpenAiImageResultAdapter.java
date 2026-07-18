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
        Objects.requireNonNull(result);

        this.images = result.data()
                .stream()
                .map(imageData -> new Image(null,imageData.base64image()))
                .toList();

        Objects.requireNonNull(result.created());
        Objects.requireNonNull(result.size());
        Objects.requireNonNull(result.outputFormat());
        Objects.requireNonNull(result.data());

        this.imageResponseMetadata = new ImageResponseMetadata(result.created());
        this.imageResponseMetadata.put(AiImageEditMetadataKeys.QUALITY,result.quality());
        this.imageResponseMetadata.put(AiImageEditMetadataKeys.OUTPUT_FORMAT,result.outputFormat());
        this.imageResponseMetadata.put(AiImageEditMetadataKeys.SIZE,result.size());
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
