package shareables.utils.timing.formatting;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
    private String formatText = "yyyy/MM/dd";
    private SimpleDateFormat formatter;

    public DateLabelFormatter() {
        formatter = new SimpleDateFormat(formatText);
    }

    @Override
    public Object stringToValue(String s) throws ParseException {
        return formatter.parseObject(s);
    }

    @Override
    public String valueToString(Object o) throws ParseException {
        if (o != null) {
            Calendar calendar = (Calendar) o;
            return formatter.format(calendar.getTime());
        }
        return "";
    }
}
