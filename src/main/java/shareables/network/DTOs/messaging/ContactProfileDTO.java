package shareables.network.DTOs.messaging;

import shareables.models.pojos.users.UserIdentifier;
import shareables.models.pojos.users.students.DegreeLevel;

public class ContactProfileDTO {
    private String contactId;
    private String contactName;
    private ContactIdentifier contactIdentifier;
    private int yearOfEntry;

    public ContactProfileDTO() {
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public ContactIdentifier getContactIdentifier() {
        return contactIdentifier;
    }

    public void setContactIdentifier(ContactIdentifier contactIdentifier) {
        this.contactIdentifier = contactIdentifier;
    }

    public int getYearOfEntry() {
        return yearOfEntry;
    }

    public void setYearOfEntry(int yearOfEntry) {
        this.yearOfEntry = yearOfEntry;
    }

    @Override
    public String toString() {
        return "<html>" +
                    contactId + " - " + contactName + " - " + contactIdentifier +
                "</html>";
    }
}