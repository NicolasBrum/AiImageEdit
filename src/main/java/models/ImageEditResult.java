package models;

import org.springframework.ai.model.ModelResult;
import org.springframework.ai.model.ResultMetadata;

public class ImageEditResult implements ModelResult<Object> {
    @Override
    public Object getOutput() {
        return null;
    }

    @Override
    public ResultMetadata getMetadata() {
        return null;
    }
}
