package app.responses;

import app.Writable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response implements Writable {
    private final String protocolVersion;
    private final int statusCode;
    private final String statusMessage;
    private static final Map<String, List<String>> headers = new HashMap<>();

    public Response(String protocolVersion, int statusCode, String statusMessage) {
        this.protocolVersion = protocolVersion;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public void setHeader(String key, String value) {
        List<String> valueList = new ArrayList<>();
        valueList.add(value);
        headers.put(key, valueList);
    }

    public void addHeader(String key, String value) {
        List<String> valueList = headers.get(key);
        valueList.add(value);
        headers.put(key, valueList);
    }

    public String getHeader(String key) {
        if (headers.containsKey(key))
            return headers.get(key).get(0);
        return null;
    }

    public int getStatusCode() {
        return statusCode;
    }

    private String getResponse() {
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
                    .append(concatHeaderValues(entry.getValue()))
                    .append("\r\n");
        }
        response.append("\r\n");
        return response.toString();
    }

    private String concatHeaderValues(List<String> values) {
        return String.join(";", values);
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        outputStream.write(getResponse().getBytes());
    }
}
