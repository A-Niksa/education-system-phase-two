package server.database.datasets;

import shareables.models.idgeneration.Identifiable;

import java.util.ArrayList;
import java.util.List;

public class Dataset<T extends Identifiable> {
    private DatasetIdentifier datasetIdentifier;
    private List<T> dataList;

    public Dataset(DatasetIdentifier datasetIdentifier) {
        this.datasetIdentifier = datasetIdentifier;
        dataList = new ArrayList<>();
    }

    public void save(T t) {
        dataList.add(t);
    }

    public void remove(String tId) {
        dataList.removeIf(e -> e.getId().equals(tId));
    }

    public T get(String tId) {
        return dataList.stream().filter(e -> e.getId().equals(tId)).findAny().orElse(null);
    }
}
