package app.responses;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseSuccess extends Response {
    private static File file;

    public ResponseSuccess(String protocolVersion, int statusCode, String statusMessage, File requestedFile) {
        super(protocolVersion, statusCode, statusMessage);
        file = requestedFile;
        buildHeaders();
    }

    private void buildHeaders() {
        setHeader("app.Server", "VladServer/08.12.2021");
        setHeader("Connection", "close");
        setHeader("Allow", "GET");
        setHeader("Content-Length", String.valueOf(file.length()));
        setContentType();
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        super.write(outputStream);
        FileInputStream inputStream = new FileInputStream(file);
        int nextByte;
        while ((nextByte = inputStream.read()) != -1) {
            outputStream.write(nextByte);
        }
        outputStream.flush();
    }

    private void setContentType() {
        int i = file.getName().lastIndexOf('.');
        String extension = "";
        if (i > 0) extension = file.getName().substring(i + 1);
        switch (extension) {
            case "txt":
                setHeader("Content-Type", "text/plain");
                break;
            case "html":
                setHeader("Content-Type", "text/html");
                break;
            case "png":
                setHeader("Content-Type", "image/png");
                break;
            default:
        }
    }
}