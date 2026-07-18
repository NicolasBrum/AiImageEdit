package io.github.nicolasbrum.core;

import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageResponseMetadata;

import java.util.List;

public interface AiImageEditResult {

    List<Image> images();

    ImageResponseMetadata metadata();
}
