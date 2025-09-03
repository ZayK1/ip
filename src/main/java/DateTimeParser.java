import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for parsing and formatting dates and times.
 */
public class DateTimeParser {

    private static final DateTimeFormatter[] INPUT_DATE_FORMATS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),        // 2019-12-02
            DateTimeFormatter.ofPattern("d/M/yyyy"),          // 2/12/2019
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),        // 02/12/2019
            DateTimeFormatter.ofPattern("d-M-yyyy"),          // 2-12-2019
            DateTimeFormatter.ofPattern("dd-MM-yyyy")         // 02-12-2019
    };

    private static final DateTimeFormatter[] INPUT_DATETIME_FORMATS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),   // 2019-12-02 1800
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm"),     // 2/12/2019 1800
            DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm"),   // 02/12/2019 1800
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),  // 2019-12-02 18:00
            DateTimeFormatter.ofPattern("d/M/yyyy HH:mm")     // 2/12/2019 18:00
    };

    private static final DateTimeFormatter OUTPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    private static final DateTimeFormatter OUTPUT_DATETIME_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    /**
     * Attempts to parse a string as a LocalDate using multiple formats.
     * @param dateStr The date string to parse
     * @return LocalDate object if parsing succeeds, null otherwise
     */
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        String trimmed = dateStr.trim();

        for (DateTimeFormatter formatter : INPUT_DATE_FORMATS) {
            try {
                return LocalDate.parse(trimmed, formatter);
            } catch (DateTimeParseException e) {
                continue;
            }
        }

        return null;
    }

    /**
     * Attempts to parse a string as a LocalDateTime using multiple formats.
     * @param dateTimeStr The date-time string to parse
     * @return LocalDateTime object if parsing succeeds, null otherwise
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }

        String trimmed = dateTimeStr.trim();

        for (DateTimeFormatter formatter : INPUT_DATETIME_FORMATS) {
            try {
                return LocalDateTime.parse(trimmed, formatter);
            } catch (DateTimeParseException e) {
                continue;
            }
        }

        return null;
    }

    /**
     * Formats a LocalDate for display.
     * @param date The date to format
     * @return Formatted date string (e.g., "Oct 15 2019")
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(OUTPUT_DATE_FORMAT);
    }

    /**
     * Formats a LocalDateTime for display.
     * @param dateTime The date-time to format
     * @return Formatted date-time string (e.g., "Oct 15 2019, 6:00PM")
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(OUTPUT_DATETIME_FORMAT);
    }

    /**
     * Checks if a date string can be parsed.
     * @param dateStr The date string to check
     * @return true if the string can be parsed as a date
     */
    public static boolean isValidDate(String dateStr) {
        return parseDate(dateStr) != null || parseDateTime(dateStr) != null;
    }
}