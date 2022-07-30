package shareables.models.pojos.academicrequests;

import shareables.models.pojos.abstractions.DepartmentName;

import java.util.Date;

public class CertificateRequest extends AcademicRequest {
    private String certificateText;

    public CertificateRequest() {
        super(AcademicRequestIdentifier.CERTIFICATE);
    }

    public void saveGeneratedCertificateText() {
        String studentName = requestingStudent.getFirstName() + " " + requestingStudent.getLastName();
        String studentId = requestingStudent.getId();
        DepartmentName majorName = AcademicRequestUtils.getDepartmentName(requestingStudent.getDepartmentId());
        Date currentDate = new Date();
        certificateText = "It is hereby certified that " + studentName + " (student ID: " + studentId +
                ") is currently a student of Sharif University of Technology and is majoring in " + majorName + "." +
                "\nDate of Certification: " + currentDate;
    }

    public String getCertificateText() {
        return AcademicRequestUtils.convertToHTMLFormat(certificateText);
    }

    public void setCertificateText(String certificateText) {
        this.certificateText = certificateText;
    }
}
