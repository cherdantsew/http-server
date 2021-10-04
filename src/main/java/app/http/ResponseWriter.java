package app.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ResponseWriter {

    public static void writeResponse(Response response, OutputStream outputStream) throws IOException {
        outputStream.write(response.toString().getBytes());
        if (response.isSuccess()) {
            try (InputStream inputStream = response.getRequestedResourceInputStream()) {
                while (inputStream.available() > 0) {
                    outputStream.write(inputStream.read());
                }
                outputStream.flush();
                return;
            }
        }
        outputStream.write(HttpErrorResponse.RM_404_HTML_TEXT.getBytes());
        outputStream.flush();
    }
}