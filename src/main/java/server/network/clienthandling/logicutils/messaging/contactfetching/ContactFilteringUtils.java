package server.network.clienthandling.logicutils.messaging.contactfetching;

import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.UserIdentifier;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.models.pojos.users.students.Student;
import shareables.models.pojos.users.students.StudentStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContactFilteringUtils {
    public static List<String> getFilteredContactIds(DatabaseManager databaseManager, List<String> contactIds,
                                                     int yearOfEntry, DegreeLevel degreeLevel, StudentStatus studentStatus) {
        List<String> filteredContactIds = new ArrayList<>();
        filteredContactIds.addAll(getFilteredStudentIds(databaseManager, contactIds, yearOfEntry, degreeLevel, studentStatus));
        filteredContactIds.addAll(getNonStudentIds(databaseManager, contactIds));

        return filteredContactIds;
    }

    private static List<String> getNonStudentIds(DatabaseManager databaseManager, List<String> contactIds) {
        return getUsersStream(databaseManager, contactIds)
                .filter(user -> user.getUserIdentifier() != UserIdentifier.STUDENT)
                .map(User::getId)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static List<String> getFilteredStudentIds(DatabaseManager databaseManager, List<String> contactIds,
                                                            int yearOfEntry, DegreeLevel degreeLevel,
                                                            StudentStatus studentStatus) {
        return getUsersStream(databaseManager, contactIds)
                .filter(user -> user.getUserIdentifier() == UserIdentifier.STUDENT)
                .map(user -> (Student) user)
                .filter(student -> student.getYearOfEntry() == yearOfEntry)
                .filter(student -> student.getDegreeLevel() == degreeLevel)
                .filter(student -> student.getStudentStatus() == studentStatus)
                .map(User::getId)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static Stream<User> getUsersStream(DatabaseManager databaseManager, List<String> userIds) {
        return userIds.stream()
                .map(id -> IdentifiableFetchingUtils.getUser(databaseManager, id));
    }
}
