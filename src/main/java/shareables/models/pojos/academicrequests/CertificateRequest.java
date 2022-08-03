package shareables.models.pojos.academicrequests;

import shareables.models.idgeneration.SequentialIdGenerator;
import shareables.models.pojos.abstractions.DepartmentName;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;
import shareables.utils.timing.formatting.FormattingUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CertificateRequest extends AcademicRequest {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator();
    }

    private String certificateText;
    private ConfigFileIdentifier configIdentifier;

    public CertificateRequest() {
        super(AcademicRequestIdentifier.CERTIFICATE);
        initializeId(sequentialIdGenerator);
        configIdentifier = ConfigFileIdentifier.ACADEMIC_REQUEST_TEXTS;
    }

    public void saveGeneratedCertificateText(String requestingStudentName, DepartmentName departmentName) {
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = FormattingUtils.getStandardDateTimeFormatter();
        certificateText = ConfigManager.getString(configIdentifier, "certificateText1") + requestingStudentName +
                ConfigManager.getString(configIdentifier, "certificateText2") + requestingStudentId +
                ConfigManager.getString(configIdentifier, "certificateText3") + departmentName +
                ConfigManager.getString(configIdentifier, "certificateText4") + dateTimeFormatter.format(currentDate);
    }

    public String fetchFormattedCertificateText() {
        return AcademicRequestSubmissionUtils.convertToHTMLFormat(certificateText);
    }

    public String getCertificateText() {
        return certificateText;
    }

    public void setCertificateText(String certificateText) {
        this.certificateText = certificateText;
    }
}
