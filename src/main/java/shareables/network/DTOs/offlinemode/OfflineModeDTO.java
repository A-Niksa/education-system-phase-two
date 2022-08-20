package shareables.network.DTOs.offlinemode;

import shareables.models.pojos.media.Picture;
import shareables.models.pojos.users.UserIdentifier;
import shareables.models.pojos.users.professors.AcademicLevel;
import shareables.models.pojos.users.professors.AcademicRole;
import shareables.models.pojos.users.students.DegreeLevel;
import shareables.models.pojos.users.students.StudentStatus;
import shareables.network.DTOs.generalmodels.CourseDTO;
import shareables.network.DTOs.messaging.ConversationThumbnailDTO;
import shareables.network.DTOs.standing.CourseScoreDTO;
import shareables.network.DTOs.standing.TranscriptDTO;

import java.time.LocalDateTime;
import java.util.List;

public class OfflineModeDTO {
    // main menu:
    private LocalDateTime lastLogin;
    private String name;
    private String emailAddress;
    private StudentStatus studentStatus;
    private String advisingProfessorName;
    private String permissionToEnrolPrompt;
    private boolean isTemporaryDeputy;
    private LocalDateTime enrolmentTime;
    private Picture profilePicture;
    private AcademicRole academicRole;
    private boolean isTimeForUnitSelection;
    // profile:
    private String id;
    private String nationalId;
    private String phoneNumber;
    private String GPAString;
    private String officeNumber;
    private int yearOfEntry;
    private String departmentId;
    private DegreeLevel degreeLevel;
    private AcademicLevel academicLevel;
    // messenger:
    private UserIdentifier userIdentifier;
    private OfflineMessengerDTO offlineMessengerDTO;
    private List<ConversationThumbnailDTO> conversationThumbnailDTOs;
    // current standing:
    private List<CourseScoreDTO> courseScoreDTOs;
    private TranscriptDTO transcriptDTO;
    // weekly schedule and exams list:
    private List<CourseDTO> courseDTOs;

    public OfflineModeDTO() {
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public StudentStatus getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(StudentStatus studentStatus) {
        this.studentStatus = studentStatus;
    }

    public String getAdvisingProfessorName() {
        return advisingProfessorName;
    }

    public void setAdvisingProfessorName(String advisingProfessorName) {
        this.advisingProfessorName = advisingProfessorName;
    }

    public String getPermissionToEnrolPrompt() {
        return permissionToEnrolPrompt;
    }

    public void setPermissionToEnrolPrompt(String permissionToEnrolPrompt) {
        this.permissionToEnrolPrompt = permissionToEnrolPrompt;
    }

    public LocalDateTime getEnrolmentTime() {
        return enrolmentTime;
    }

    public void setEnrolmentTime(LocalDateTime enrolmentTime) {
        this.enrolmentTime = enrolmentTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGPAString() {
        return GPAString;
    }

    public void setGPAString(String GPAString) {
        this.GPAString = GPAString;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    public int getYearOfEntry() {
        return yearOfEntry;
    }

    public void setYearOfEntry(int yearOfEntry) {
        this.yearOfEntry = yearOfEntry;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public DegreeLevel getDegreeLevel() {
        return degreeLevel;
    }

    public void setDegreeLevel(DegreeLevel degreeLevel) {
        this.degreeLevel = degreeLevel;
    }

    public AcademicLevel getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(AcademicLevel academicLevel) {
        this.academicLevel = academicLevel;
    }

    public OfflineMessengerDTO getOfflineMessengerDTO() {
        return offlineMessengerDTO;
    }

    public void setOfflineMessengerDTO(OfflineMessengerDTO offlineMessengerDTO) {
        this.offlineMessengerDTO = offlineMessengerDTO;
    }

    public TranscriptDTO getTranscriptDTO() {
        return transcriptDTO;
    }

    public void setTranscriptDTO(TranscriptDTO transcriptDTO) {
        this.transcriptDTO = transcriptDTO;
    }

    public List<CourseDTO> getCourseDTOs() {
        return courseDTOs;
    }

    public void setCourseDTOs(List<CourseDTO> courseDTOs) {
        this.courseDTOs = courseDTOs;
    }

    public Picture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Picture profilePicture) {
        this.profilePicture = profilePicture;
    }

    public boolean isTemporaryDeputy() {
        return isTemporaryDeputy;
    }

    public void setTemporaryDeputy(boolean temporaryDeputy) {
        isTemporaryDeputy = temporaryDeputy;
    }

    public AcademicRole getAcademicRole() {
        return academicRole;
    }

    public void setAcademicRole(AcademicRole academicRole) {
        this.academicRole = academicRole;
    }

    public List<CourseScoreDTO> getCourseScoreDTOs() {
        return courseScoreDTOs;
    }

    public void setCourseScoreDTOs(List<CourseScoreDTO> courseScoreDTOs) {
        this.courseScoreDTOs = courseScoreDTOs;
    }

    public UserIdentifier getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(UserIdentifier userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public List<ConversationThumbnailDTO> getConversationThumbnailDTOs() {
        return conversationThumbnailDTOs;
    }

    public void setConversationThumbnailDTOs(List<ConversationThumbnailDTO> conversationThumbnailDTOs) {
        this.conversationThumbnailDTOs = conversationThumbnailDTOs;
    }

    public boolean isTimeForUnitSelection() {
        return isTimeForUnitSelection;
    }

    public void setTimeForUnitSelection(boolean timeForUnitSelection) {
        isTimeForUnitSelection = timeForUnitSelection;
    }

    // TODO: to be removed
    @Override
    public String toString() {
        return "OfflineModeDTO{" +
                "lastLogin=" + lastLogin +
                ", name='" + name + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", studentStatus=" + studentStatus +
                ", advisingProfessorName='" + advisingProfessorName + '\'' +
                ", permissionToEnrolPrompt='" + permissionToEnrolPrompt + '\'' +
                ", isTemporaryDeputy=" + isTemporaryDeputy +
                ", enrolmentTime=" + enrolmentTime +
                ", profilePicture=" + profilePicture +
                ", academicRole=" + academicRole +
                ", id='" + id + '\'' +
                ", nationalId='" + nationalId + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", GPAString='" + GPAString + '\'' +
                ", officeNumber='" + officeNumber + '\'' +
                ", yearOfEntry=" + yearOfEntry +
                ", departmentId=" + departmentId +
                ", degreeLevel=" + degreeLevel +
                ", academicLevel=" + academicLevel +
                ", offlineMessengerDTO=" + offlineMessengerDTO +
                ", transcriptDTO=" + transcriptDTO +
                ", courseDTOs=" + courseDTOs +
                '}';
    }
}