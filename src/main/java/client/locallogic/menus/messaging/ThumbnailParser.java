package client.locallogic.menus.messaging;

public class ThumbnailParser {
    public static String getIdFromThumbnailText(String thumbnailText, String separator) {
        String selectedIdWithHTMLTag = thumbnailText.split(separator)[0];
        return selectedIdWithHTMLTag.substring(6); // removing "<html>"
    }

    public static String getItemTypeString(String thumbnailText, String separator) {
        String[] partitionedThumbnail = thumbnailText.split(separator);
        String selectedItemTypeWithHTMLTag = partitionedThumbnail[partitionedThumbnail.length - 1];
        int stringLength = selectedItemTypeWithHTMLTag.length();
        return selectedItemTypeWithHTMLTag.substring(0, stringLength - 7); // removing "</html>"
    }
}
