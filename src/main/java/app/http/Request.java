package app.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {

    private String method;
    private String resource;
    private String protocolVersion;
    private final Map<String, List<String>> headers = new HashMap<>();

    public void setHeader(String key, String value) {
        if (this.headers.containsKey(key)) {
            List<String> values = this.headers.get(key);
            values.add(value);
            this.headers.put(key, values);
            return;
        }
        List<String> valueList = new ArrayList<>();
        valueList.add(value);
        this.headers.put(key, valueList);
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
}
