package io.github.nicolasbrum.models;

import org.jspecify.annotations.Nullable;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.model.ModelOptions;
import org.springframework.ai.model.ModelRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

public class AiImageEditPrompt implements ModelRequest<List<String>> {
    private List<String> instructions;
    private ImageOptions imageOptions;
    private Resource resource;
    private MediaType mediaType;

    private AiImageEditPrompt() {}

    public AiImageEditPrompt(Builder builder) {
        this.instructions = builder.instructions;
        this.imageOptions = builder.imageOptions;
        this.resource = builder.resource;
        this.mediaType = builder.mediaType;
    }

    public static Builder builder(){
        return new Builder();
    }

    @Override
    public List<String> getInstructions() {
        return List.copyOf(instructions);
    }

    @Override
    public @Nullable ModelOptions getOptions() {
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

        public Builder addInstruction(String instruction){
            this.instructions.add(instruction);
            return this;
        }

        public Builder instruction(String instruction){
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
                throw new IllegalArgumentException("resource must not be null.");
            }
            if(this.instructions == null){
                throw new IllegalArgumentException("instructions must not be null.");
            }
            if(this.mediaType == null){
                throw new IllegalArgumentException("mediaType must not be null.");
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
            return "gpt-image-1";
        }

        @Override
        public @Nullable Integer getWidth() {
            return 1080;
        }

        @Override
        public @Nullable Integer getHeight() {
            return 800;
        }

        @Override
        public @Nullable String getResponseFormat() {
            return "jpeg";
        }

        @Override
        public @Nullable String getStyle() {
            return "normal";
        }
    }

}
