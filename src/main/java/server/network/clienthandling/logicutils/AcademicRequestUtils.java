package server.network.clienthandling.logicutils;

import server.database.management.DatabaseManager;
import shareables.models.pojos.academicrequests.AcademicRequestStatus;
import shareables.models.pojos.academicrequests.CertificateRequest;
import shareables.models.pojos.academicrequests.DormRequest;
import shareables.models.pojos.users.students.Student;

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
}
