package app.responses;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseError extends Response {
    private static String errorResponse;

    public ResponseError(String protocolVersion, int statusCode, String statusMessage, String errorResponseMessage) {
        super(protocolVersion, statusCode, statusMessage);
        errorResponse = errorResponseMessage;
        buildHeaders();
    }

    private void buildHeaders() {
        setHeader("app.Server", "VladServer/08.12.2021");
        setHeader("Connection", "close");
        setHeader("Allow", "GET");
        setHeader("Content-Length", String.valueOf(errorResponse.length()));
        setHeader("Content-Type", "text/html");
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        super.write(outputStream);
        outputStream.write(errorResponse.getBytes());
        outputStream.flush();
    }
}
