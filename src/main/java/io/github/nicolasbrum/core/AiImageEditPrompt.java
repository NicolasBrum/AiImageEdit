package io.github.nicolasbrum.core;

import io.github.nicolasbrum.exceptions.AiImageEditPromptException;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.model.ModelOptions;
import org.springframework.ai.model.ModelRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AiImageEditPrompt implements ModelRequest<List<String>> {
    private List<String> instructions;
    private ImageOptions imageOptions;
    private Resource resource;
    private MediaType mediaType;

    private AiImageEditPrompt() {}

    private AiImageEditPrompt(Builder builder) {
        this.instructions = List.copyOf(builder.instructions);
        this.imageOptions = builder.imageOptions;
        this.resource = builder.resource;
        this.mediaType = builder.mediaType;
    }

    public static Builder builder(){
        return new Builder();
    }

    @Override
    public @NonNull List<String> getInstructions() {
        return List.copyOf(instructions);
    }

    @Override
    public @NonNull ModelOptions getOptions() {
        return this.imageOptions;
    }

    public Resource getResource() {
        return resource;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public static class Builder{
        private List<String> instructions = new ArrayList<>();
        private ImageOptions imageOptions;
        private Resource resource;
        private MediaType mediaType;

        private Builder(){}

        public Builder instructions(List<String> instructions){
            this.instructions = new ArrayList<>(instructions);
            return this;
        }

        public Builder instruction(String instruction){
            Objects.requireNonNull(instruction);
            this.instructions.add(instruction);
            return this;
        }

        public Builder imageOptions(ImageOptions imageOptions){
            this.imageOptions = imageOptions;
            return this;
        }

        public Builder resource(Resource resource){
            this.resource = resource;
            return this;
        }

        public Builder mediaType(MediaType mediaType){
            this.mediaType = mediaType;
            return this;
        }

        public AiImageEditPrompt build(){
            if(this.imageOptions == null){
                this.imageOptions = new DefaultImageEditOptions();
            }
            if(this.resource == null){
                throw new AiImageEditPromptException("resource must not be null or empty.");
            }
            if(this.instructions == null || this.instructions.isEmpty()){
                throw new AiImageEditPromptException("instructions must not be null.");
            }
            if(this.mediaType == null){
                throw new AiImageEditPromptException("mediaType must not be null.");
            }

            return new AiImageEditPrompt(this);
        }
    }

    private static class DefaultImageEditOptions implements ImageOptions{

        @Override
        public @Nullable Integer getN() {
            return 1;
        }

        @Override
        public @Nullable String getModel() {
            return null;
        }

        @Override
        public @Nullable Integer getWidth() {
            return null;
        }

        @Override
        public @Nullable Integer getHeight() {
            return null;
        }

        @Override
        public @Nullable String getResponseFormat() {
            return null;
        }

        @Override
        public @Nullable String getStyle() {
            return null;
        }
    }

}
