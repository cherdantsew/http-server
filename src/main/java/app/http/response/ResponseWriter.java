package app.http.response;

import java.io.*;

public class ResponseWriter {
    public void writeResponse(Response response, OutputStream outputStream, int bufferSize) throws IOException {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, bufferSize);
        bufferedOutputStream.write(response.toString().getBytes());
        byte[] bytes = new byte[bufferSize];
        try (InputStream inputStream = new BufferedInputStream(response.getInputStream(), bufferSize)) {
            while (inputStream.read(bytes) != -1) {
                bufferedOutputStream.write(bytes);
            }
            bufferedOutputStream.flush();
        }
    }
}