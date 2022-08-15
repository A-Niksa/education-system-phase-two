package shareables.models.pojos.abstractions;

import shareables.models.idgeneration.IdentifiableWithTime;
import shareables.models.pojos.users.students.DegreeLevel;

public class UnitSelectionSession extends IdentifiableWithTime {
    private DegreeLevel intendedDegreeLevel;
    private int intendedYearOfEntry;

    public UnitSelectionSession() {
        initializeId();
    }

    public DegreeLevel getIntendedDegreeLevel() {
        return intendedDegreeLevel;
    }

    public void setIntendedDegreeLevel(DegreeLevel intendedDegreeLevel) {
        this.intendedDegreeLevel = intendedDegreeLevel;
    }

    public int getIntendedYearOfEntry() {
        return intendedYearOfEntry;
    }

    public void setIntendedYearOfEntry(int intendedYearOfEntry) {
        this.intendedYearOfEntry = intendedYearOfEntry;
    }
}