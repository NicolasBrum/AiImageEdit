package models;

import org.jspecify.annotations.Nullable;
import org.springframework.ai.model.ModelResponse;
import org.springframework.ai.model.ResponseMetadata;

import java.util.List;

public class ImageEditResponse implements ModelResponse<ImageEditResult> {
    @Override
    public @Nullable ImageEditResult getResult() {
        return null;
    }

    @Override
    public List getResults() {
        return List.of();
    }

    @Override
    public ResponseMetadata getMetadata() {
        return null;
    }
}
