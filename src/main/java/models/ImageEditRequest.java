package models;

import org.jspecify.annotations.Nullable;
import org.springframework.ai.model.ModelOptions;
import org.springframework.ai.model.ModelRequest;

public class ImageEditRequest implements ModelRequest<Object> {
    @Override
    public Object getInstructions() {
        return null;
    }

    @Override
    public @Nullable ModelOptions getOptions() {
        return null;
    }
}
