package app.http.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response {
    //private final InputStream inputStream;
    private final String protocolVersion;
    private final int statusCode;
    private final String statusMessage;
    private final String filePath;
    private final Map<String, List<String>> headers = new HashMap<>();
    //private final boolean isCached;

    public Response(String protocolVersion, int statusCode, String statusMessage, String fileName) {
        this.protocolVersion = protocolVersion;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        //this.inputStream = inputStream;
        //this.isCached = isCached;
        this.filePath = fileName;
        buildHeaders();
    }

    public void setHeader(String key, String value) {
        List<String> valueList = new ArrayList<>();
        valueList.add(value);
        headers.put(key, valueList);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getFilePath() {
        return filePath;
    }

        /*public InputStream getInputStream() {
        return inputStream;
    }

    public boolean isCached() {
        return isCached;
    }
*/

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
        setHeader("app.server.Server", "VladServer/08.12.2021");
        setHeader("Connection", "close");
        setHeader("Allow", "GET");
    }
}