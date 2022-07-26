package client.controller;

import shareables.network.requests.Request;
import shareables.network.requests.RequestIdentifier;

public class RequestGenerator {
    public Request generateRequest(RequestIdentifier requestIdentifier, StringObjectMap... stringObjectMaps) {
        Request request = new Request(requestIdentifier);
        for (StringObjectMap stringObjectMap : stringObjectMaps) {
            request.put(stringObjectMap.getString(), stringObjectMap.getObject());
        }
        return request;
    }
}