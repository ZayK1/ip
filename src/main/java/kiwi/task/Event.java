package kiwi.task;

import kiwi.storage.DateTimeParser;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Event extends Task {
    private String fromString;
    private String toString;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;

    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.fromString = from;
        this.toString = to;
        parseDateTime(from, to);
    }

    /**
     * Attempts to parse the from/to strings as dates/times.
     */
    private void parseDateTime(String from, String to) {
        this.fromDateTime = DateTimeParser.parseDateTime(from);
        this.toDateTime = DateTimeParser.parseDateTime(to);

        if (this.fromDateTime == null) {
            this.fromDate = DateTimeParser.parseDate(from);
        }

        if (this.toDateTime == null) {
            this.toDate = DateTimeParser.parseDate(to);
        }
    }

    public String getFrom() {
        return fromString;
    }

    public String getTo() {
        return toString;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public LocalDateTime getFromDateTime() {
        return fromDateTime;
    }

    public LocalDateTime getToDateTime() {
        return toDateTime;
    }

    /**
     * Returns true if this event has parsed date/time information.
     */
    public boolean hasDateTime() {
        return (fromDate != null || fromDateTime != null) &&
                (toDate != null || toDateTime != null);
    }

    /**
     * Gets the formatted from date/time string for display.
     */
    public String getFormattedFromDateTime() {
        if (fromDateTime != null) {
            return DateTimeParser.formatDateTime(fromDateTime);
        } else if (fromDate != null) {
            return DateTimeParser.formatDate(fromDate);
        } else {
            return fromString;
        }
    }

    /**
     * Gets the formatted to date/time string for display.
     */
    public String getFormattedToDateTime() {
        if (toDateTime != null) {
            return DateTimeParser.formatDateTime(toDateTime);
        } else if (toDate != null) {
            return DateTimeParser.formatDate(toDate);
        } else {
            return toString;
        }
    }

    /**
     * Gets the start date for this event.
     */
    public LocalDate getStartDate() {
        if (fromDate != null) {
            return fromDate;
        } else if (fromDateTime != null) {
            return fromDateTime.toLocalDate();
        }
        return null;
    }

    /**
     * Gets the end date for this event.
     */
    public LocalDate getEndDate() {
        if (toDate != null) {
            return toDate;
        } else if (toDateTime != null) {
            return toDateTime.toLocalDate();
        }
        return null;
    }

    @Override
    public String toString() {
        String fromStr = getFormattedFromDateTime();
        String toStr = getFormattedToDateTime();
        return taskType.toString() + "[" + (this.done ? "X" : " ") + "] " + this.description +
                " (from: " + fromStr + " to: " + toStr + ")";
    }
}