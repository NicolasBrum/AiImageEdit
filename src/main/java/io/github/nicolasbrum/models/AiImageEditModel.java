package io.github.nicolasbrum.models;

import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.model.Model;

public interface AiImageEditModel extends Model<AiImageEditPrompt, ImageResponse> {
}
