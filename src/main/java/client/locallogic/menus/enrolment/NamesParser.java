package client.locallogic.menus.enrolment;

public class NamesParser {
    public static String[] parseDelimitedNames(String names) {
        return names.split(", ");
    }
}
