package app.http.response;

import java.io.*;

public class ResponseWriter {
    public void writeResponse(Response response, OutputStream outputStream, int bufferSize) throws IOException {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, bufferSize);
        bufferedOutputStream.write(response.toString().getBytes());
        try (InputStream inputStream = new BufferedInputStream(response.getInputStream(), bufferSize)) {
            int data;
            while ((data = inputStream.read()) != -1) {
                bufferedOutputStream.write(data);
            }
            bufferedOutputStream.flush();
        }
    }
}