package app.http.response;

import app.server.ServerCache;
import app.http.HTTPUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ResponseWriter {
    public void writeResponse(Response response, OutputStream outputStream, int bufferSize, ServerCache serverCache) throws IOException {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, bufferSize);
        bufferedOutputStream.write(response.toString().getBytes());
        if (response.getStatusCode() == HTTPUtils.RC_OK) {
            if (serverCache.contains(response.getFilePath())) {
                writeFromCache(response, serverCache, bufferedOutputStream);
                return;
            }
            writeFromInputStream(response, bufferedOutputStream, bufferSize);
            return;
        } else if (response.getStatusCode() == HTTPUtils.RC_NOT_FOUND) {
            bufferedOutputStream.write(HttpErrorResponse.RM_404_HTML_TEXT.getBytes(StandardCharsets.UTF_8));
            bufferedOutputStream.flush();
            return;
        }
        bufferedOutputStream.write(HttpErrorResponse.RM_401_HTML_TEXT.getBytes(StandardCharsets.UTF_8));
        bufferedOutputStream.flush();
    }

    private void writeFromCache(Response response, ServerCache serverCache, BufferedOutputStream bufferedOutputStream) throws IOException {
        bufferedOutputStream.write(serverCache.getResource(response.getFilePath()));
        bufferedOutputStream.flush();
    }

    private void writeFromInputStream(Response response, BufferedOutputStream bufferedOutputStream, int bufferSize) throws IOException {
        byte[] bytes = new byte[bufferSize];
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(response.getFilePath()), bufferSize)) {
            while (inputStream.read(bytes) != -1) {
                bufferedOutputStream.write(bytes);
            }
            bufferedOutputStream.flush();
        }
    }
}