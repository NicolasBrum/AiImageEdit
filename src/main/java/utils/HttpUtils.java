package utils;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


public class HttpUtils {

    public static HttpHeaders createHeadersForFile(ByteArrayResource fileBytes){

        HttpHeaders filePartHeaders = new HttpHeaders();
        filePartHeaders.setContentType(MediaType.IMAGE_JPEG);
        filePartHeaders.setContentDisposition(ContentDisposition.formData()
                .name("image")
                .filename(fileBytes.getFilename())
                .build());

        return filePartHeaders;
    }

}
