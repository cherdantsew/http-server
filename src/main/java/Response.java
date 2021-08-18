import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Response {

    private String protocolVersion;
    private int statusCode;
    private String statusMessage;

    private final Map<String, String> headers = new HashMap<>();
    private byte[] body = null;

    public static final int RC_OK = 200;
    public static final int RC_NOT_FOUND = 404;
    public static final int RC_METHOD_NOT_ALLOWED = 405;
    public static final String RM_OK = "OK";
    public static final String RM_FILE_NOT_FOUND = "FILE NOT FOUND ON THE SERVER";
    public static final String RM_METHOD_NOT_ALLOWED = "THIS METHOD IS NOT ALLOWED";
    public static final String HTTP_1_1_PROTOCOL = "HTTP/1.1";

    public Response(String protocolVersion, int statusCode, String statusMessage) {
        this.protocolVersion = protocolVersion;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public void addHeader(String name, String value) {
        this.headers.put(name, value);
    }

    public void addBody(byte[] bytes) {
        this.body = bytes;
    }

    public byte[] getBody() {
        return this.body;
    }

    public String getResponse() {
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
                    .append(entry.getValue())
                    .append("\r\n");
        }
        response.append("\r\n");
        return response.toString();
    }
}