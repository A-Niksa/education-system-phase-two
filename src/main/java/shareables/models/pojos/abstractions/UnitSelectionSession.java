package shareables.models.pojos.abstractions;

import shareables.models.idgeneration.IdentifiableWithTime;
import shareables.models.pojos.users.students.DegreeLevel;

import java.time.LocalDateTime;

public class UnitSelectionSession extends IdentifiableWithTime {
    private DegreeLevel intendedDegreeLevel;
    private int intendedYearOfEntry;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;

    public UnitSelectionSession() {
        initializeId();
    }

    public void resetSession() {
        // TODO
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

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }
}