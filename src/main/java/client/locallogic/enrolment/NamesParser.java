package client.locallogic.enrolment;

public class NamesParser {
    public static String[] parseDelimitedNames(String names) {
        return names.split(", ");
    }
}
