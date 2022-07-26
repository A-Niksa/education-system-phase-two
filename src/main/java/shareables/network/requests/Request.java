package shareables.network.requests;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String authToken;
    private RequestIdentifier requestIdentifier;
    private Map<String, Object> dataMap;

    public Request() {
    }

    public Request(RequestIdentifier requestIdentifier) {
        this.requestIdentifier = requestIdentifier;
        dataMap = new HashMap<>();
    }

    public void put(String dataName, Object data) {
        dataMap.put(dataName, data);
    }

    public Object get(String dataName) {
        return dataMap.get(dataName);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public RequestIdentifier getRequestIdentifier() {
        return requestIdentifier;
    }

    public void setRequestIdentifier(RequestIdentifier requestIdentifier) {
        this.requestIdentifier = requestIdentifier;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }
}
