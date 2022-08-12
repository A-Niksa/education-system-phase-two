package client.locallogic.messaging;

public class ThumbnailIdParser {
    public static String getIdFromConversationThumbnailText(String conversationThumbnailText) {
        String selectedIdWithHTMLTag = conversationThumbnailText.split(" - ")[0];
        return selectedIdWithHTMLTag.substring(6); // removing "<html>"
    }
}
