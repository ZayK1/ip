package kiwi;

import kiwi.parser.Parser;
import kiwi.storage.Storage;
import kiwi.storage.DateTimeParser;
import kiwi.task.TaskList;
import kiwi.task.Task;
import kiwi.task.Todo;
import kiwi.task.Deadline;
import kiwi.task.Event;
import kiwi.exception.KiwiException;
import kiwi.exception.UnknownCommandException;

import java.time.LocalDate;
import java.util.List;

/**
 * Main class for the Kiwi task manager applications.
 */
public class Kiwi {
    private Storage storage;
    private TaskList tasks;

    /**
     * Constructs a new Kiwi application with the specified file path for data storage.
     */
    public Kiwi(String filePath) {
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (KiwiException e) {
            tasks = new TaskList();
        }
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            Parser.CommandType commandType = Parser.getCommandType(input);

            switch (commandType) {
                case BYE:
                    return "Bye! Hope to see you again soon!";

                case LIST:
                    return handleListCommand();

                case MARK:
                    return handleMarkCommand(input);

                case UNMARK:
                    return handleUnmarkCommand(input);

                case DELETE:
                    return handleDeleteCommand(input);

                case TODO:
                    return handleTodoCommand(input);

                case DEADLINE:
                    return handleDeadlineCommand(input);

                case EVENT:
                    return handleEventCommand(input);

                case ON:
                    return handleOnCommand(input);

                case FIND:
                    return handleFindCommand(input);

                case UNKNOWN:
                default:
                    throw new UnknownCommandException();
            }

        } catch (KiwiException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "Something unexpected happened: " + e.getMessage();
        }
    }

    private String handleListCommand() {
        if (tasks.size() == 0) {
            return "You have no tasks in your list.";
        }
        return "Here are the tasks in your list:\n" + tasks.toString();
    }

    private String handleMarkCommand(String command) throws KiwiException {
        int taskNumber = Parser.parseTaskNumber(command, "mark");
        int index = taskNumber - 1;

        if (!tasks.isValidIndex(index)) {
            throw new KiwiException("Task number " + taskNumber + " doesn't exist");
        }

        tasks.markTask(index);
        saveTasksToStorage();
        return "Nice! I've marked this task as done:\n" + tasks.getTask(index);
    }

    private String handleUnmarkCommand(String command) throws KiwiException {
        int taskNumber = Parser.parseTaskNumber(command, "unmark");
        int index = taskNumber - 1;

        if (!tasks.isValidIndex(index)) {
            throw new KiwiException("Task number " + taskNumber + " doesn't exist");
        }

        tasks.unmarkTask(index);
        saveTasksToStorage();
        return "Okay, I've marked this task as not done yet:\n" + tasks.getTask(index);
    }

    private String handleDeleteCommand(String command) throws KiwiException {
        int taskNumber = Parser.parseTaskNumber(command, "delete");
        int index = taskNumber - 1;

        if (!tasks.isValidIndex(index)) {
            throw new KiwiException("Task number " + taskNumber + " doesn't exist");
        }

        Task deletedTask = tasks.deleteTask(index);
        saveTasksToStorage();
        return "Noted. I've removed this task:\n" + deletedTask +
                "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleTodoCommand(String command) throws KiwiException {
        String description = Parser.parseTodoCommand(command);
        Todo todo = new Todo(description);
        tasks.addTask(todo);
        saveTasksToStorage();
        return "Got it. I've added this task:\n" + todo +
                "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleDeadlineCommand(String command) throws KiwiException {
        String[] parts = Parser.parseDeadlineCommand(command);
        String description = parts[0];
        String deadline = parts[1];

        Deadline task = new Deadline(description, deadline);
        tasks.addTask(task);
        saveTasksToStorage();
        return "Got it. I've added this task:\n" + task +
                "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleEventCommand(String command) throws KiwiException {
        String[] parts = Parser.parseEventCommand(command);
        String description = parts[0];
        String from = parts[1];
        String to = parts[2];

        Event event = new Event(description, from, to);
        tasks.addTask(event);
        saveTasksToStorage();
        return "Got it. I've added this task:\n" + event +
                "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleOnCommand(String command) throws KiwiException {
        LocalDate targetDate = Parser.parseOnCommand(command);
        List<Task> tasksOnDate = tasks.getTasksOnDate(targetDate);
        String formattedDate = DateTimeParser.formatDate(targetDate);

        if (tasksOnDate.isEmpty()) {
            return "No tasks found on " + formattedDate + ".";
        } else {
            StringBuilder response = new StringBuilder("Here are the tasks on " + formattedDate + ":\n");
            for (int i = 0; i < tasksOnDate.size(); i++) {
                response.append(i + 1).append(".").append(tasksOnDate.get(i)).append("\n");
            }
            return response.toString();
        }
    }

    private String handleFindCommand(String command) throws KiwiException {
        String keyword = Parser.parseFindCommand(command);
        List<Task> foundTasks = tasks.findTasks(keyword);

        if (foundTasks.isEmpty()) {
            return "No matching tasks found.";
        } else {
            StringBuilder response = new StringBuilder("Here are the matching tasks in your list:\n");
            for (int i = 0; i < foundTasks.size(); i++) {
                response.append(i + 1).append(".").append(foundTasks.get(i)).append("\n");
            }
            return response.toString();
        }
    }

    private void saveTasksToStorage() {
        try {
            storage.saveTasks(tasks.getTasks());
        } catch (KiwiException e) {
            // Silent fail for GUI, could implement error dialog if needed
        }
    }
}