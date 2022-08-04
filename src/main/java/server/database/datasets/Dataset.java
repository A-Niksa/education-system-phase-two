package server.database.datasets;

import shareables.models.idgeneration.Identifiable;

import java.util.ArrayList;
import java.util.List;

public class Dataset {
    private List<Identifiable> identifiables;

    public Dataset() {
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
