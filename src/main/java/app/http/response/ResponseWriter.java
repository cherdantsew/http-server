package app.http.response;

import app.ServerCache;
import app.http.HTTPUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ResponseWriter {
    public void writeResponse(Response response, OutputStream outputStream, int bufferSize, ServerCache serverCache) throws IOException {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, bufferSize);
        bufferedOutputStream.write(response.toString().getBytes());
        if (response.getStatusCode() == HTTPUtils.RC_OK) {
            if (serverCache.contains(response.getFileName())){
                writeFromCache(response, serverCache, bufferedOutputStream);
                return;
            }
            writeFromInputStream(response, bufferedOutputStream, bufferSize);
            return;
        }
        bufferedOutputStream.write(HttpErrorResponse.RM_404_HTML_TEXT.getBytes(StandardCharsets.UTF_8));
        bufferedOutputStream.flush();
    }

    private void writeFromCache(Response response, ServerCache serverCache, BufferedOutputStream bufferedOutputStream) throws IOException {
        bufferedOutputStream.write(serverCache.getResource(response.getFileName()));
        bufferedOutputStream.flush();
    }

    private void writeFromInputStream(Response response, BufferedOutputStream bufferedOutputStream, int bufferSize) throws IOException {
        byte[] bytes = new byte[bufferSize];
        try (InputStream inputStream = new BufferedInputStream(new  FileInputStream(response.getFileName()), bufferSize)) {
            while (inputStream.read(bytes) != -1) {
                bufferedOutputStream.write(bytes);
            }
            bufferedOutputStream.flush();
        }
    }
}