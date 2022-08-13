package server.network.clienthandling.logicutils.services;

import server.database.datasets.DatasetIdentifier;
import server.database.management.DatabaseManager;
import server.network.clienthandling.logicutils.comparators.RequestDTOComparator;
import server.network.clienthandling.logicutils.general.IdentifiableFetchingUtils;
import shareables.models.idgeneration.Identifiable;
import shareables.models.pojos.academicrequests.AcademicRequestStatus;
import shareables.models.pojos.academicrequests.DroppingOutRequest;
import shareables.models.pojos.academicrequests.RecommendationRequest;
import shareables.models.pojos.users.professors.Professor;
import shareables.models.pojos.users.students.Student;
import shareables.models.pojos.users.students.StudentStatus;
import shareables.network.DTOs.academicrequests.RequestDTO;

import java.util.ArrayList;
import java.util.List;

public class RequestManagementUtils {
    private static RequestDTOComparator comparator = new RequestDTOComparator();

    public static List<RequestDTO> getDroppingOutRequestDTOs(DatabaseManager databaseManager, String departmentId) {
        List<Identifiable> droppingOutRequests = databaseManager.getIdentifiables(DatasetIdentifier.DROPPING_OUT_REQUESTS);
        List<RequestDTO> droppingOutRequestDTOs = new ArrayList<>();
        droppingOutRequests.stream()
                .map(e -> (DroppingOutRequest) e)
                .filter(e -> {
                    Student requestingStudent = IdentifiableFetchingUtils.getStudent(databaseManager, e.getRequestingStudentId());
                    return requestingStudent.getDepartmentId().equals(departmentId);
                })
                .filter(e -> e.getRequestStatus() == AcademicRequestStatus.SUBMITTED)
                .forEach(e -> {
                    RequestDTO requestDTO = new RequestDTO();
                    requestDTO.setId(e.getId());
                    requestDTO.setRequestingStudentId(e.getRequestingStudentId());
                    Student requestingStudent = IdentifiableFetchingUtils.getStudent(databaseManager, e.getRequestingStudentId());
                    requestDTO.setRequestingStudentName(requestingStudent.fetchName());
                    droppingOutRequestDTOs.add(requestDTO);
                });
        droppingOutRequestDTOs.sort(comparator);
        return droppingOutRequestDTOs;
    }

    public static void acceptDroppingOutRequest(DatabaseManager databaseManager, String requestingStudentId,
                                                String academicRequestId) {
        DroppingOutRequest droppingOutRequest = (DroppingOutRequest) databaseManager.get(
                DatasetIdentifier.DROPPING_OUT_REQUESTS, academicRequestId);
        droppingOutRequest.setRequestStatus(AcademicRequestStatus.ACCEPTED);
        Student student = IdentifiableFetchingUtils.getStudent(databaseManager, requestingStudentId);
        student.setStudentStatus(StudentStatus.DROPPED_OUT);
    }

    public static void declineDroppingOutRequest(DatabaseManager databaseManager, String academicRequestId) {
        DroppingOutRequest droppingOutRequest = (DroppingOutRequest) databaseManager.get(
                DatasetIdentifier.DROPPING_OUT_REQUESTS, academicRequestId);
        droppingOutRequest.setRequestStatus(AcademicRequestStatus.DECLINED);
    }

    public static void acceptRecommendationRequest(DatabaseManager databaseManager, String academicRequestId) {
        RecommendationRequest recommendationRequest = (RecommendationRequest) databaseManager.get(
                DatasetIdentifier.RECOMMENDATION_REQUESTS, academicRequestId);
        recommendationRequest.setRequestStatus(AcademicRequestStatus.ACCEPTED);
        Professor receivingProfessor = IdentifiableFetchingUtils.getProfessor(databaseManager,
                recommendationRequest.getReceivingProfessorId());
        Student requestingStudent = IdentifiableFetchingUtils.getStudent(databaseManager,
                recommendationRequest.getRequestingStudentId());
        recommendationRequest.saveGeneratedRecommendationText(receivingProfessor.fetchName(),
                requestingStudent.fetchName());
    }

    public static void declineRecommendationRequest(DatabaseManager databaseManager, String academicRequestId) {
        RecommendationRequest recommendationRequest = (RecommendationRequest) databaseManager.get(
                DatasetIdentifier.RECOMMENDATION_REQUESTS, academicRequestId);
        recommendationRequest.setRequestStatus(AcademicRequestStatus.DECLINED);
    }

    public static List<RequestDTO> getProfessorRecommendationRequestDTOs(DatabaseManager databaseManager,
                                                                         String receivingProfessorId) {
        List<Identifiable> recommendationRequests = databaseManager.getIdentifiables(DatasetIdentifier.RECOMMENDATION_REQUESTS);
        List<RequestDTO> recommendationRequestDTOs = new ArrayList<>();
        recommendationRequests.stream()
                .map(e -> (RecommendationRequest) e)
                .filter(e -> e.getReceivingProfessorId().equals(receivingProfessorId))
                .filter(e -> e.getRequestStatus() == AcademicRequestStatus.SUBMITTED)
                .forEach(e -> {
                    RequestDTO requestDTO = new RequestDTO();
                    requestDTO.setId(e.getId());
                    requestDTO.setRequestingStudentId(e.getRequestingStudentId());
                    Student student = IdentifiableFetchingUtils.getStudent(databaseManager, e.getRequestingStudentId());
                    requestDTO.setRequestingStudentName(student.fetchName());
                    requestDTO.setRequestingStudentGPAString(student.fetchGPAString());
                    recommendationRequestDTOs.add(requestDTO);
                });
        recommendationRequestDTOs.sort(comparator);
        return recommendationRequestDTOs;
    }
}