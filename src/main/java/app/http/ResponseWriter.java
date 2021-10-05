package app.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ResponseWriter {

    public static void writeResponse(Response response, OutputStream outputStream, int bufferSize) throws IOException {
        outputStream.write(response.toString().getBytes());
        if (response.isSuccessfull()) {
            byte[] bytes = new byte[bufferSize];
            int size;
            try (InputStream inputStream = new BufferedInputStream(response.getRequestedResourceInputStream(), bufferSize)) {
                while ((size = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, size);
                }
                outputStream.flush();
                return;
            }
        }
        outputStream.write(HttpErrorResponse.RM_404_HTML_TEXT.getBytes());
        outputStream.flush();
    }
}