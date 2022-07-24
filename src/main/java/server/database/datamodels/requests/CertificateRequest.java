package server.database.datamodels.requests;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
public class CertificateRequest extends Request {
    @Column
    private String certificateText;

    public void saveGeneratedCertificateText() {
        String studentName = requestingStudent.getFirstName() + " " + requestingStudent.getLastName();
        String studentId = requestingStudent.getId();
        String majorName = requestingStudent.getDepartment().getDepartmentName().toString();
        Date currentDate = new Date();
        certificateText = "It is hereby certified that " + studentName + " (student ID: " + studentId +
                ") is currently a student of Sharif University of Technology and is majoring in " + majorName + "." +
                "\nDate of Certification: " + currentDate;
    }

    public String getCertificateText() {
        return RequestTextProcessor.convertToHTMLFormat(certificateText);
    }

    public void setCertificateText(String certificateText) {
        this.certificateText = certificateText;
    }
}
