package shareables.network.DTOs.messaging;

public enum ContactIdentifier {
    UNDERGRADUATE_STUDENT("Undergraduate Student"),
    GRADUATE_STUDENT("Graduate Student"),
    PHD_STUDENT("PhD Student"),
    PROFESSOR("Professor"),
    ADVISING_PROFESSOR("Advising Professor"),
    MR_MOHSENI("Mr. Mohseni"),
    ADMIN("Admin");

    private String contactIdentifierString;

    ContactIdentifier(String contactIdentifierString) {
        this.contactIdentifierString = contactIdentifierString;
    }

    @Override
    public String toString() {
        return contactIdentifierString;
    }
}
