package app.http.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {

    private static final int HEADER_PARAM_NAME_INDEX = 0;
    private static final int HEADER_PARAM_VALUE_INDEX = 1;
    private static final String HEADER_PARAM_NAME_AND_VALUE_DELIMITER = "=";
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

    public String getParamValueFromHeader(String headerName, String paramName) {
        if (headers.get(headerName) != null) {
            for (String headerCompositeValue : headers.get(headerName)) {
                String[] keyValueArray = headerCompositeValue.split(HEADER_PARAM_NAME_AND_VALUE_DELIMITER);
                if (paramName.equals(keyValueArray[HEADER_PARAM_NAME_INDEX]))
                    return keyValueArray[HEADER_PARAM_VALUE_INDEX];
            }
        }
        return null;
    }

    public void printRequestHeaders() {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            System.out.print(entry.getKey() + ": ");
            for (String values : entry.getValue())
                System.out.print(values + ";");
            System.out.println();
        }
    }
}