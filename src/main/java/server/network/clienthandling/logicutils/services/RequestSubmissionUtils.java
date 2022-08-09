package server.network.clienthandling.logicutils.services;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.general.EnumStringMappingUtils;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.academicrequests.*;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;
import shareables.network.requests.Request;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestSubmissionUtils {
    public static boolean willGetDorm() {
        DormRequest dormRequest = new DormRequest();
        return dormRequest.getRequestStatus() == AcademicRequestStatus.ACCEPTED;
    }

    public static String getCertificateText(DatabaseManager databaseManager, String studentId) {
        CertificateRequest certificateRequest = new CertificateRequest();
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, studentId);
        certificateRequest.setRequestingStudentId(studentId);
        certificateRequest.saveGeneratedCertificateText(student.fetchName(),
                EnumStringMappingUtils.getDepartmentName(student.getDepartmentId()));
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
            defenseRequest.setRequestingStudentId((String) request.get("username"));
            databaseManager.save(DatasetIdentifier.DEFENSE_REQUESTS, defenseRequest);
        }
    }

    private static DefenseRequest getDefenseRequest(DatabaseManager databaseManager, String studentId) {
        List<Identifiable> defenseRequests = databaseManager.getIdentifiables(DatasetIdentifier.DEFENSE_REQUESTS);
        DefenseRequest studentDefenseRequest = defenseRequests.stream()
                .map(e -> (DefenseRequest) e)
                .filter(e -> e.getRequestingStudentId().equals(studentId))
                .findAny().orElse(null);
        return studentDefenseRequest;
    }

    public static boolean studentHasSubmittedDroppingOutRequest(DatabaseManager databaseManager, String studentId) {
        DroppingOutRequest droppingOutRequest = getDroppingOutRequest(databaseManager, studentId);
        return droppingOutRequest != null &&
                droppingOutRequest.getRequestStatus() == AcademicRequestStatus.SUBMITTED;
    }

    public static AcademicRequestStatus getDroppingOutRequestStatus(DatabaseManager databaseManager, String studentId) {
        DroppingOutRequest droppingOutRequest = getDroppingOutRequest(databaseManager, studentId);
        if (droppingOutRequest == null) return null;
        return droppingOutRequest.getRequestStatus();
    }

    public static DroppingOutRequest getDroppingOutRequest(DatabaseManager databaseManager, String studentId) {
        List<Identifiable> droppingOutRequests = databaseManager.getIdentifiables(DatasetIdentifier.DROPPING_OUT_REQUESTS);
        DroppingOutRequest droppingOutRequest = droppingOutRequests.stream()
                .map(e -> (DroppingOutRequest) e)
                .filter(e -> e.getRequestingStudentId().equals(studentId))
                .findAny().orElse(null);
        return droppingOutRequest;
    }

    public static RecommendationRequest getRecommendationRequest(DatabaseManager databaseManager, String requestingStudentId,
                                                                 String receivingProfessorId) {
        List<Identifiable> recommendationRequests = databaseManager.getIdentifiables(DatasetIdentifier.RECOMMENDATION_REQUESTS);
        RecommendationRequest recommendationRequest = recommendationRequests.stream()
                .map(e -> (RecommendationRequest) e)
                .filter(e -> e.getRequestingStudentId().equals(requestingStudentId) &&
                        e.getReceivingProfessorId().equals(receivingProfessorId))
                .findAny().orElse(null);
        return recommendationRequest;
    }

    public static void submitDroppingOutRequest(DatabaseManager databaseManager, String studentId) {
        DroppingOutRequest droppingOutRequest = getDroppingOutRequest(databaseManager, studentId);
        if (droppingOutRequest == null) {
            DroppingOutRequest newDroppingOutRequest = new DroppingOutRequest();
            Student student = IdentifiableFetchingUtils.getStudent(databaseManager, studentId);
            newDroppingOutRequest.setRequestingStudentId(studentId);
            newDroppingOutRequest.setReceivingProfessorId(IdentifiableFetchingUtils.getDepartmentDeputyId(databaseManager,
                    student.getDepartmentId()));
            databaseManager.save(DatasetIdentifier.DROPPING_OUT_REQUESTS, newDroppingOutRequest);
        }
    }

    public static boolean professorExists(DatabaseManager databaseManager, String professorId) {
        Professor professor = IdentifiableFetchingUtils.getProfessor(databaseManager, professorId);
        return professor != null;
    }

    public static void submitRecommendationRequest(DatabaseManager databaseManager, String requestingStudentId,
                                                   String receivingProfessorId) {
        RecommendationRequest recommendationRequest = getRecommendationRequest(databaseManager, requestingStudentId,
                receivingProfessorId);
        if (recommendationRequest == null) {
            RecommendationRequest newRecommendationRequest = new RecommendationRequest();
            newRecommendationRequest.setRequestingStudentId(requestingStudentId);
            newRecommendationRequest.setReceivingProfessorId(receivingProfessorId);
            databaseManager.save(DatasetIdentifier.RECOMMENDATION_REQUESTS, newRecommendationRequest);
        }
    }

    public static List<String> getStudentRecommendationTexts(DatabaseManager databaseManager, String username) {
        List<Identifiable> recommendationRequests = databaseManager.getIdentifiables(DatasetIdentifier.RECOMMENDATION_REQUESTS);
        List<String> recommendationTexts = new ArrayList<>();
        recommendationRequests.stream()
                .map(e -> (RecommendationRequest) e)
                .filter(e -> e.getRequestStatus() == AcademicRequestStatus.ACCEPTED)
                .forEach(e -> recommendationTexts.add(e.fetchFormattedRecommendationText()));
        return recommendationTexts;
    }
}