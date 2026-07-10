package models;

import org.jspecify.annotations.Nullable;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.model.ModelOptions;
import org.springframework.ai.model.ModelRequest;
import org.springframework.core.io.ByteArrayResource;

import java.util.List;

public class ImageEditPrompt implements ModelRequest<List<String>> {
    private List<String> instructions;
    private ImageOptions imageOptions;
    private ByteArrayResource resource;

    private ImageEditPrompt() {}

    public static ImageEditPrompt builder(){
        return new ImageEditPrompt();
    }

    public ImageEditPrompt instructions(List<String> instructions){
        this.instructions = instructions;
        return this;
    }

    public ImageEditPrompt addInstruction(String instruction){
        this.instructions.add(instruction);
        return this;
    }

    public ImageEditPrompt imageOptions(ImageOptions imageOptions){
        this.imageOptions = imageOptions;
        return this;
    }

    public ImageEditPrompt resource(ByteArrayResource resource){
        this.resource = resource;
        return this;
    }

    public ImageEditPrompt build(){
        if(this.imageOptions == null){
            this.imageOptions = new ImageEditOptionsDefault();
        }

        return this;
    }

    @Override
    public List<String> getInstructions() {
        return this.instructions;
    }

    @Override
    public @Nullable ModelOptions getOptions() {
        return this.imageOptions;
    }

    public ByteArrayResource getResource() {
        return resource;
    }

    private class ImageEditOptionsDefault implements ImageOptions{

        @Override
        public @Nullable Integer getN() {
            return 1;
        }

        @Override
        public @Nullable String getModel() {
            return "";
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
            return "";
        }

        @Override
        public @Nullable String getStyle() {
            return "normal";
        }
    }

}
