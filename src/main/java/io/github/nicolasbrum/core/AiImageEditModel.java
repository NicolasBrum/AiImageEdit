package io.github.nicolasbrum.core;

import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.model.Model;
import org.springframework.stereotype.Component;

@Component
public interface AiImageEditModel extends Model<AiImageEditPrompt, ImageResponse> {
}
