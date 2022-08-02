package shareables.models.pojos.academicrequests;

import shareables.models.idgeneration.SequentialIdGenerator;
import shareables.models.pojos.abstractions.DepartmentName;
import shareables.utils.timing.formatting.FormattingUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CertificateRequest extends AcademicRequest {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator();
    }

    private String certificateText;

    public CertificateRequest() {
        super(AcademicRequestIdentifier.CERTIFICATE);
        initializeId(sequentialIdGenerator);
    }

    public void saveGeneratedCertificateText() {
        String studentName = requestingStudent.fetchName();
        String studentId = requestingStudent.getId();
        DepartmentName majorName = AcademicRequestUtils.getDepartmentName(requestingStudent.getDepartmentId());
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = FormattingUtils.getStandardDateTimeFormatter();
        certificateText = "It is hereby certified that " + studentName + " (student ID: " + studentId +
                ") is currently a student of Sharif University of Technology and is majoring in " + majorName + "." +
                "\nDate of Certification: " + dateTimeFormatter.format(currentDate);
    }

    public String fetchFormattedCertificateText() {
        return AcademicRequestUtils.convertToHTMLFormat(certificateText);
    }

    public String getCertificateText() {
        return certificateText;
    }

    public void setCertificateText(String certificateText) {
        this.certificateText = certificateText;
    }
}
