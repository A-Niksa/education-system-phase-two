package server.network.clienthandling.logicutils;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.academicrequests.AcademicRequestStatus;
import shareables.models.pojos.academicrequests.CertificateRequest;
import shareables.models.pojos.academicrequests.DefenseRequest;
import shareables.models.pojos.academicrequests.DormRequest;
import shareables.models.pojos.users.students.Student;
import shareables.network.requests.Request;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class AcademicRequestUtils {
    public static boolean willGetDorm() {
        DormRequest dormRequest = new DormRequest();
        return dormRequest.getRequestStatus() == AcademicRequestStatus.ACCEPTED;
    }

    public static String getCertificateText(DatabaseManager databaseManager, String studentId) {
        CertificateRequest certificateRequest = new CertificateRequest();
        Student student = (Student) LoginUtils.getUser(databaseManager, studentId);
        certificateRequest.setRequestingStudent(student);
        certificateRequest.saveGeneratedCertificateText();
        return certificateRequest.fetchFormattedCertificateText();
    }

    public static LocalDateTime getDefenseTime(DatabaseManager databaseManager, String studentId) {
        DefenseRequest studentDefenseRequest = getDefenseRequest(databaseManager, studentId);
        return studentDefenseRequest == null ? null : studentDefenseRequest.getRequestedDefenseTime();
    }

    public static boolean dateIsSoonerThanNow(Date date, int hour, int minute) {
        LocalDateTime convertedDate = convertDateToLocalDateTime(date, hour, minute);
        return convertedDate.compareTo(LocalDateTime.now()) <= 0;
    }

    private static LocalDateTime convertDateToLocalDateTime(Date date, int hour, int minute) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return localDateTime.withHour(hour).withMinute(minute).withSecond(0);
    }

    public static void reserveDefenseTime(DatabaseManager databaseManager, Request request) {
        DefenseRequest previousDefenseRequest = getDefenseRequest(databaseManager, (String) request.get("username"));
        if (previousDefenseRequest != null) {
            previousDefenseRequest.setRequestedDefenseTime((LocalDateTime) request.get("defenseTime"));
        } else {
            DefenseRequest defenseRequest = new DefenseRequest();
            LocalDateTime defenseTime = convertDateToLocalDateTime((Date) request.get("date"), (int) request.get("hour"),
                    (int) request.get("minute"));
            defenseRequest.setRequestedDefenseTime(defenseTime);
            Student student = (Student) LoginUtils.getUser(databaseManager, (String) request.get("username"));
            defenseRequest.setRequestingStudent(student);
            databaseManager.save(DatasetIdentifier.DEFENSE_REQUESTS, defenseRequest);
        }
    }

    private static DefenseRequest getDefenseRequest(DatabaseManager databaseManager, String studentId) {
        List<Identifiable> defenseRequests = databaseManager.getIdentifiables(DatasetIdentifier.DEFENSE_REQUESTS);
        DefenseRequest studentDefenseRequest = defenseRequests.stream()
                .map(e -> (DefenseRequest) e)
                .filter(e -> e.getRequestingStudent().getId().equals(studentId))
                .findAny().orElse(null);
        return studentDefenseRequest;
    }
}