package client.locallogic.services;

import shareables.utils.timing.formatting.FormattingUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeFormatter {
    /**
     * uses the extensive DateTimeFormatter, which includes hour and minute
     */
    public static String formatExtensively(LocalDateTime localDateTime) {
        DateTimeFormatter dateTimeFormatter = FormattingUtils.getExtensiveDateTimeFormatter();
        return dateTimeFormatter.format(localDateTime);
    }
}
