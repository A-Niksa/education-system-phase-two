package shareables.network.responses;

import java.util.HashMap;
import java.util.Map;

public class Response {
    private Map<String, Object> dataMap;
    private ResponseStatus responseStatus;
    private String errorMessage;
    private String unsolicitedMessage;

    public Response() {
    }

    public Response(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
        dataMap = new HashMap<>();
    }

    public Response(String errorMessage) {
        this.errorMessage = errorMessage;
        responseStatus = ResponseStatus.ERROR;
        dataMap = new HashMap<>();
    }

    public Response(ResponseStatus responseStatus, String unsolicitedMessage) {
        this.responseStatus = responseStatus;
        this.unsolicitedMessage = unsolicitedMessage;
    }

    public void put(String dataName, Object data) {
        dataMap.put(dataName, data);
    }

    public Object get(String dataName) {
        return dataMap.get(dataName);
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getUnsolicitedMessage() {
        return unsolicitedMessage;
    }

    public void setUnsolicitedMessage(String unsolicitedMessage) {
        this.unsolicitedMessage = unsolicitedMessage;
    }
}
