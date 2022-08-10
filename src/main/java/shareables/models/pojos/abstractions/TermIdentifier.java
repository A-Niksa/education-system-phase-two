package shareables.models.pojos.abstractions;

import java.time.LocalDateTime;

public class TermIdentifier {
    private int year;
    private int semester;

    public TermIdentifier() {

    }

    public TermIdentifier(int year, int semester) {
        this.year = year;
        if (1 <= semester && semester <= 2) this.semester = semester;
    }

    public boolean courseIsActive() {
        LocalDateTime currentDate = LocalDateTime.now();
        boolean courseIsInThisYear = year == currentDate.getYear();
        boolean courseIsInThisSemester = semester == fetchCurrentSemester(currentDate);
        return courseIsInThisYear && courseIsInThisSemester;
    }

    private int fetchCurrentSemester(LocalDateTime currentDate) {
        int currentMonth = currentDate.getMonthValue();
        return currentMonth <= 6 ? 1 : 2;
    }

    public String fetchTermIdentifierString() {
        return year + "-" + semester;
    }

    @Override
    public String toString() {
        return (year % 2000) + String.format("%1d", semester);
    }
}
