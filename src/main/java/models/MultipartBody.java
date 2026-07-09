package models;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MultipartBody {

    private final String CRLF = "\n\r";
    private final String boundary;
    private final ByteArrayOutputStream bodyBytes;

    private MultipartBody() {
        this.boundary = "------" + UUID.randomUUID();
        this.bodyBytes = new ByteArrayOutputStream();
    }

    public static MultipartBody builder(){
        return new MultipartBody();
    }

    private void setContentType() throws IOException {
        this.bodyBytes.write(("Content-Type: multipart/form-data; boundary=" + this.boundary + CRLF).getBytes());
    }

    public MultipartBody addTextPart(String name, String value){
        try{
            bodyBytes.write(("--" + boundary + CRLF).getBytes());

            bodyBytes.write(
                    ("Content-Disposition: form-data; name=\"" + name + "\"" + CRLF)
                            .getBytes());

            bodyBytes.write(
                    ("Content-Type: text/plain" + CRLF)
                            .getBytes());

            bodyBytes.write(CRLF.getBytes());

            bodyBytes.write(value.getBytes(StandardCharsets.UTF_8));

            bodyBytes.write(CRLF.getBytes());
        }catch (Exception e){

        }
        return this;
    }
}
