package client.locallogic.menus.messaging;

public class ThumbnailIdParser {
    public static String getIdFromThumbnailText(String thumbnailText, String separator) {
        String selectedIdWithHTMLTag = thumbnailText.split(separator)[0];
        return selectedIdWithHTMLTag.substring(6); // removing "<html>"
    }
}
