package client.locallogic.localdatabase.datasets;

import shareables.models.idgeneration.Identifiable;

import java.util.ArrayList;
import java.util.List;

public class LocalDataset {
    private List<Identifiable> identifiables;

    public LocalDataset() {
        identifiables = new ArrayList<>();
    }

    public void save(Identifiable identifiable) {
        identifiables.add(identifiable);
    }

    public void remove(String identifiableId) {
        identifiables.removeIf(e -> e.getId().equals(identifiableId));
    }

    public Identifiable get(String identifiableId) {
        return identifiables.stream()
                .filter(e -> e.getId().equals(identifiableId))
                .findAny().orElse(null);
    }

    public List<Identifiable> getIdentifiables() {
        return identifiables;
    }
}
