package server.database.datamodels.academicrequests;

import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorOptions(force = true)
public class CertificateRequest extends AcademicRequest {
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
        return AcademicRequestUtils.convertToHTMLFormat(certificateText);
    }

    public void setCertificateText(String certificateText) {
        this.certificateText = certificateText;
    }
}
