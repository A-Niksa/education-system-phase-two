package client.locallogic.general;

import org.jdatepicker.impl.UtilDateModel;

import java.time.LocalDateTime;

public class DatePickerConfigurationTool {
    public static void configureInitialDateOfDatePicker(UtilDateModel dateModel) {
        LocalDateTime currentTime = LocalDateTime.now();
        dateModel.setDate(currentTime.getYear(), currentTime.getMonthValue() - 1, currentTime.getDayOfMonth());
        dateModel.setSelected(true);
    }
}
