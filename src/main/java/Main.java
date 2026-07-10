import models.AiImageEditClient;
import models.ImageEditClient;
import models.ImageEditPrompt;
import models.OpenAiImageEditModel;
import org.springframework.core.io.ByteArrayResource;

public class Main {
    public static void main(String[] args) {

        var prompt = ImageEditPrompt.builder()
                .addInstruction("Trocar a cor do carro para vermelho.")
                .resource(new ByteArrayResource(new byte[10]))
                .build();

        var imageEditClient = ImageEditClient.create(AiImageEditClient.OPENAI);
        var imageEditModel = new OpenAiImageEditModel();
        imageEditModel.call(prompt);
    }
}
