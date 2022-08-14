package client.locallogic.menus.messaging;

public class ThumbnailIdParser {
    public static String getIdFromThumbnailText(String conversationThumbnailText, String separator) {
        String selectedIdWithHTMLTag = conversationThumbnailText.split(separator)[0];
        return selectedIdWithHTMLTag.substring(6); // removing "<html>"
    }
}
