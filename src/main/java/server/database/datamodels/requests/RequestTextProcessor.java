package server.database.datamodels.requests;

public class RequestTextProcessor {
    public static String convertToHTMLFormat(String text) {
        return "<html>" + text.replaceAll("\n", "<br>");
    }
}
