import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Request {

    private String method;
    private String resource;
    private String protocolVersion;
    private Map<String, List<String>> headers = new HashMap<>();

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

    public String getHeader(String name) {
        if (this.headers.containsKey(name))
            return this.headers.get(name).get(0);
        return null;
    }
}
