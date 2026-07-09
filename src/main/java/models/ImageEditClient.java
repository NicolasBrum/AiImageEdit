package models;

import java.net.http.HttpClient;

public class ImageEditClient {

    public static HttpClient create(AiImageEditClient type){
        switch (type){
            case OPENAI -> new OpenAiImageEditClient();
        }

    }
}
