package client.controller;

import shareables.network.requests.Request;
import shareables.network.requests.RequestIdentifier;

import java.util.ArrayList;
import java.util.List;

public class RequestGenerator {
    public Request generateRequest(RequestIdentifier requestIdentifier, StringObjectMap... stringObjectMaps) {
        return generateRequest(requestIdentifier, new ArrayList<>(List.of(stringObjectMaps)));
    }

    public Request generateRequest(RequestIdentifier requestIdentifier, List<StringObjectMap> stringObjectMaps) {
        Request request = new Request(requestIdentifier);
        for (StringObjectMap stringObjectMap : stringObjectMaps) {
            request.put(stringObjectMap.getString(), stringObjectMap.getObject());
        }
        return request;
    }
}