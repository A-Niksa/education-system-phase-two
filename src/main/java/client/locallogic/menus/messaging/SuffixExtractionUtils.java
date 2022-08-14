package client.locallogic.menus.messaging;

public class SuffixExtractionUtils {
    public static String getFileSuffix(String filePath) {
        StringBuilder fileSuffix = new StringBuilder();
        int charIndex = filePath.length() - 1;
        while (filePath.charAt(charIndex) != '.') {
            fileSuffix.insert(0, filePath.charAt(charIndex));
            charIndex--;
        }
        fileSuffix.insert(0, '.');
        return fileSuffix.toString().toLowerCase();
    }
}
