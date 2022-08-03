package shareables.network.DTOs;

public class RequestDTO {
    private String id;
    private String requestingStudentId;
    private String requestingStudentName;
    private String requestingStudentGPAString;

    public RequestDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestingStudentId() {
        return requestingStudentId;
    }

    public void setRequestingStudentId(String requestingStudentId) {
        this.requestingStudentId = requestingStudentId;
    }

    public String getRequestingStudentName() {
        return requestingStudentName;
    }

    public void setRequestingStudentName(String requestingStudentName) {
        this.requestingStudentName = requestingStudentName;
    }

    public String getRequestingStudentGPAString() {
        return requestingStudentGPAString;
    }

    public void setRequestingStudentGPAString(String requestingStudentGPAString) {
        this.requestingStudentGPAString = requestingStudentGPAString;
    }
}
