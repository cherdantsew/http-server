package app.http;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response {
    private final InputStream requestedResourceInputStream;
    private final String protocolVersion;
    private final int statusCode;
    private final String statusMessage;
    private final boolean isSuccess;
    private final Map<String, List<String>> headers = new HashMap<>();

    public Response(String protocolVersion, int statusCode, String statusMessage, InputStream requestedResourceInputStream, boolean isSuccess) {
        this.protocolVersion = protocolVersion;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.requestedResourceInputStream = requestedResourceInputStream;
        this.isSuccess = isSuccess;
        buildHeaders();
    }

    public void setHeader(String key, String value) {
        List<String> valueList = new ArrayList<>();
        valueList.add(value);
        headers.put(key, valueList);
    }

    public InputStream getRequestedResourceInputStream() {
        return requestedResourceInputStream;
    }

    public boolean isSuccessfull() {
        return isSuccess;
    }

    public String toString() {
        StringBuilder response = new StringBuilder();
        response.append(protocolVersion)
                .append(" ")
                .append(statusCode)
                .append(" ")
                .append(statusMessage)
                .append("\r\n");
        for (var entry : headers.entrySet()) {
            response.append(entry.getKey())
                    .append(": ")
                    .append(String.join(";", entry.getValue()))
                    .append("\r\n");
        }
        response.append("\r\n");
        return response.toString();
    }

    private void buildHeaders() {
        setHeader("app.Server", "VladServer/08.12.2021");
        setHeader("Connection", "close");
        setHeader("Allow", "GET");
    }
}