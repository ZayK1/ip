package kiwi.parser;

import kiwi.exception.KiwiException;
import kiwi.exception.EmptyDescriptionException;
import kiwi.storage.DateTimeParser;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Parses user input and determines the appropriate command and parameters.
 */
public class Parser {

    public enum CommandType {
        BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, ON, UNKNOWN
    }

    /**
     * Parses the user input and returns the command type.
     */
    public static CommandType getCommandType(String input) {
        String trimmed = input.trim().toLowerCase();

        if (trimmed.equals("bye")) {
            return CommandType.BYE;
        } else if (trimmed.equals("list")) {
            return CommandType.LIST;
        } else if (trimmed.startsWith("mark ")) {
            return CommandType.MARK;
        } else if (trimmed.startsWith("unmark ")) {
            return CommandType.UNMARK;
        } else if (trimmed.startsWith("delete ")) {
            return CommandType.DELETE;
        } else if (trimmed.startsWith("todo")) {
            return CommandType.TODO;
        } else if (trimmed.startsWith("deadline")) {
            return CommandType.DEADLINE;
        } else if (trimmed.startsWith("event")) {
            return CommandType.EVENT;
        } else if (trimmed.startsWith("on ")) {
            return CommandType.ON;
        } else {
            return CommandType.UNKNOWN;
        }
    }

    /**
     * Parses a todo command and returns the description.
     */
    public static String parseTodoCommand(String input) throws KiwiException {
        if (input.trim().equals("todo") || input.length() <= 4 || input.substring(4).trim().isEmpty()) {
            throw new EmptyDescriptionException("todo");
        }
        return input.substring(5).trim();
    }

    /**
     * Parses a deadline command and returns description and deadline.
     */
    public static String[] parseDeadlineCommand(String input) throws KiwiException {
        if (input.trim().equals("deadline")) {
            throw new EmptyDescriptionException("deadline");
        }

        String withoutCommand = input.substring(9).trim();
        int byIndex = withoutCommand.indexOf("/by");

        if (byIndex == -1) {
            throw new KiwiException("Deadline format should be: deadline <description> /by <time>");
        }

        String description = withoutCommand.substring(0, byIndex).trim();
        String deadline = withoutCommand.substring(byIndex + 3).trim();

        if (description.isEmpty()) {
            throw new EmptyDescriptionException("deadline");
        }
        if (deadline.isEmpty()) {
            throw new KiwiException("Deadline time cannot be empty");
        }

        return new String[]{description, deadline};
    }

    /**
     * Parses an event command and returns description, from, and to.
     */
    public static String[] parseEventCommand(String input) throws KiwiException {
        if (input.trim().equals("event")) {
            throw new EmptyDescriptionException("event");
        }

        String withoutCommand = input.substring(6).trim();
        int fromIndex = withoutCommand.indexOf("/from");

        if (fromIndex == -1) {
            throw new KiwiException("Event format should be: event <description> /from <start> /to <end>");
        }

        String description = withoutCommand.substring(0, fromIndex).trim();
        String timeInfo = withoutCommand.substring(fromIndex + 5);
        int toIndex = timeInfo.indexOf("/to");

        if (toIndex == -1) {
            throw new KiwiException("Event must have both /from and /to times");
        }

        String from = timeInfo.substring(0, toIndex).trim();
        String to = timeInfo.substring(toIndex + 3).trim();

        if (description.isEmpty()) {
            throw new EmptyDescriptionException("event");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new KiwiException("Event start and end times cannot be empty");
        }

        return new String[]{description, from, to};
    }

    /**
     * Parses a mark/unmark command and returns the task number.
     */
    public static int parseTaskNumber(String input, String commandType) throws KiwiException {
        try {
            String[] parts = input.split(" ");
            if (parts.length < 2) {
                throw new KiwiException("Please specify which task to " + commandType + "!");
            }
            int taskId = Integer.parseInt(parts[1]);
            return taskId;
        } catch (NumberFormatException e) {
            throw new KiwiException("Please provide a valid task number to " + commandType + "!");
        }
    }

    /**
     * Parses an "on" command and returns the date.
     */
    public static LocalDate parseOnCommand(String input) throws KiwiException {
        String dateStr = input.substring(3).trim();

        if (dateStr.isEmpty()) {
            throw new KiwiException("Please specify a date! Format: on yyyy-mm-dd or on d/m/yyyy");
        }

        LocalDate targetDate = DateTimeParser.parseDate(dateStr);
        if (targetDate == null) {
            throw new KiwiException("Invalid date format! Try: on 2019-12-02 or on 2/12/2019");
        }

        return targetDate;
    }
}