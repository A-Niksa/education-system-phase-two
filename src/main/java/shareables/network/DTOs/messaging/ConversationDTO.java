package shareables.network.DTOs.messaging;

import shareables.models.pojos.media.Picture;
import shareables.models.pojos.messaging.Conversation;

public class ConversationDTO {
    private Conversation conversation;
    private String contactId;
    private String contactName;
    private Picture contactProfilePicture;

    public ConversationDTO() {
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
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

    public Picture getContactProfilePicture() {
        return contactProfilePicture;
    }

    public void setContactProfilePicture(Picture contactProfilePicture) {
        this.contactProfilePicture = contactProfilePicture;
    }
}
