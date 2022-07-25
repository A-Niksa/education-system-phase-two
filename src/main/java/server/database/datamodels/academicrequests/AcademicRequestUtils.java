package server.database.datamodels.academicrequests;

public class AcademicRequestUtils {
    public static String convertToHTMLFormat(String text) {
        return "<html>" + text.replaceAll("\n", "<br>");
    }
}
