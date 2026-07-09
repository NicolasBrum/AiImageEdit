package models;

import org.jspecify.annotations.Nullable;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.model.ModelOptions;
import org.springframework.ai.model.ModelRequest;
import org.springframework.core.io.ByteArrayResource;

import java.util.List;

public class ImageEditPrompt implements ModelRequest<Object> {
    private List<String> instructions;
    private ImageOptions imageOptions;
    private ByteArrayResource resource;

    private ImageEditPrompt() {}

    public static ImageEditPrompt builder(){
        return new ImageEditPrompt();
    }

    @Override
    public Object getInstructions() {
        return null;
    }

    @Override
    public @Nullable ModelOptions getOptions() {
        return null;
    }

}
