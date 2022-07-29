package shareables.models.idgeneration;

import shareables.models.pojos.messaging.Chat;
import shareables.models.pojos.messaging.Message;

import java.util.Date;

public class IdGenerator {
    public static String generateId(Message message) {
        Date messageDate = message.getMessageDate();
        return messageDate.getTime() + "";
    }

    public static String generateId(Chat chat) {
        Date dateOfCreation = chat.getDateOfCreation();
        return dateOfCreation.getTime() + "";
    }
}
