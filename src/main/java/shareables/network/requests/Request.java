package shareables.network.requests;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String authToken;
    private RequestIdentifier requestIdentifier;
    private Map<String, Object> dataMap;

    public Request(RequestIdentifier requestIdentifier) {
        this.authToken = authToken;
        this.requestIdentifier = requestIdentifier;
        dataMap = new HashMap<>();
    }

    public void put(String dataName, Object data) {
        this.authToken = authToken;
        dataMap.put(dataName, data);
    }

    public Object get(String dataName) {
        this.authToken = authToken;
        return dataMap.get(dataName);
    }

    public RequestIdentifier getRequestIdentifier() {
        return requestIdentifier;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
