package shareables.network.blueprints;

import client.controller.StringObjectMap;

import java.util.ArrayList;
import java.util.List;

public class Blueprint {
    private List<StringObjectMap> fields; // in reality, acts like a Map<String, Object>

    public Blueprint() {
        fields = new ArrayList<>();
    }

    public void put(String fieldName, Object fieldValue) {
        fields.add(new StringObjectMap(fieldName, fieldValue));
    }

    public List<StringObjectMap> getFields() {
        return fields;
    }
}
