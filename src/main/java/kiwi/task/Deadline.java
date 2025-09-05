package kiwi.task;

import kiwi.storage.DateTimeParser;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Deadline extends Task {
    private String byString;
    private LocalDate byDate;
    private LocalDateTime byDateTime;

    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.byString = by;
        parseDateTime(by);
    }

    /**
     * Attempts to parse the deadline string as a date/time.
     */
    private void parseDateTime(String by) {
        this.byDateTime = DateTimeParser.parseDateTime(by);

        if (this.byDateTime == null) {
            this.byDate = DateTimeParser.parseDate(by);
        }
    }

    public String getBy() {
        return byString;
    }

    public LocalDate getByDate() {
        return byDate;
    }

    public LocalDateTime getByDateTime() {
        return byDateTime;
    }

    /**
     * Returns true if this deadline has a parsed date/time.
     */
    public boolean hasDateTime() {
        return byDate != null || byDateTime != null;
    }

    /**
     * Gets the formatted date/time string for display.
     */
    public String getFormattedDateTime() {
        if (byDateTime != null) {
            return DateTimeParser.formatDateTime(byDateTime);
        } else if (byDate != null) {
            return DateTimeParser.formatDate(byDate);
        } else {
            return byString;
        }
    }

    /**
     * Gets the LocalDate for this deadline (either from byDate or byDateTime).
     */
    public LocalDate getDate() {
        if (byDate != null) {
            return byDate;
        } else if (byDateTime != null) {
            return byDateTime.toLocalDate();
        }
        return null;
    }

    @Override
    public String toString() {
        String dateTimeStr = getFormattedDateTime();
        return taskType.toString() + "[" + (this.done ? "X" : " ") + "] " + this.description + " (by: " + dateTimeStr + ")";
    }
}