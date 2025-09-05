
package kiwi;

import kiwi.ui.Ui;
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
 * Main class for the Kiwi task manager application.
 */
public class Kiwi {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public Kiwi(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (KiwiException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Main run loop for the application.
     */
    public void run() {
        ui.showWelcome();

        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();

                Parser.CommandType commandType = Parser.getCommandType(fullCommand);

                switch (commandType) {
                    case BYE:
                        handleByeCommand();
                        isExit = true;
                        break;
                    case LIST:
                        handleListCommand();
                        break;
                    case MARK:
                        handleMarkCommand(fullCommand);
                        break;
                    case UNMARK:
                        handleUnmarkCommand(fullCommand);
                        break;
                    case DELETE:
                        handleDeleteCommand(fullCommand);
                        break;
                    case TODO:
                        handleTodoCommand(fullCommand);
                        break;
                    case DEADLINE:
                        handleDeadlineCommand(fullCommand);
                        break;
                    case EVENT:
                        handleEventCommand(fullCommand);
                        break;
                    case ON:
                        handleOnCommand(fullCommand);
                        break;
                    case UNKNOWN:
                    default:
                        throw new UnknownCommandException();
                }

            } catch (KiwiException e) {
                ui.showError(e.getMessage());
            } catch (Exception e) {
                ui.showError("Something unexpected happened: " + e.getMessage());
            }
        }

        ui.close();
    }

    private void handleByeCommand() {
        ui.showGoodbye();
    }

    private void handleListCommand() {
        ui.showTaskList(tasks);
    }

    private void handleMarkCommand(String command) throws KiwiException {
        int taskNumber = Parser.parseTaskNumber(command, "mark");
        int index = taskNumber - 1;

        if (!tasks.isValidIndex(index)) {
            throw new KiwiException("Task number " + taskNumber + " doesn't exist");
        }

        tasks.markTask(index);
        saveTasksToStorage();
        ui.showTaskMarked(tasks.getTask(index));
    }

    private void handleUnmarkCommand(String command) throws KiwiException {
        int taskNumber = Parser.parseTaskNumber(command, "unmark");
        int index = taskNumber - 1;

        if (!tasks.isValidIndex(index)) {
            throw new KiwiException("Task number " + taskNumber + " doesn't exist");
        }

        tasks.unmarkTask(index);
        saveTasksToStorage();
        ui.showTaskUnmarked(tasks.getTask(index));
    }

    private void handleDeleteCommand(String command) throws KiwiException {
        int taskNumber = Parser.parseTaskNumber(command, "delete");
        int index = taskNumber - 1;

        if (!tasks.isValidIndex(index)) {
            throw new KiwiException("Task number " + taskNumber + " doesn't exist");
        }

        Task deletedTask = tasks.deleteTask(index);
        saveTasksToStorage();
        ui.showTaskDeleted(deletedTask, tasks.size());
    }

    private void handleTodoCommand(String command) throws KiwiException {
        String description = Parser.parseTodoCommand(command);
        Todo todo = new Todo(description);
        tasks.addTask(todo);
        saveTasksToStorage();
        ui.showTaskAdded(todo, tasks.size());
    }

    private void handleDeadlineCommand(String command) throws KiwiException {
        String[] parts = Parser.parseDeadlineCommand(command);
        String description = parts[0];
        String deadline = parts[1];

        Deadline task = new Deadline(description, deadline);
        tasks.addTask(task);
        saveTasksToStorage();
        ui.showTaskAdded(task, tasks.size());
    }

    private void handleEventCommand(String command) throws KiwiException {
        String[] parts = Parser.parseEventCommand(command);
        String description = parts[0];
        String from = parts[1];
        String to = parts[2];

        Event event = new Event(description, from, to);
        tasks.addTask(event);
        saveTasksToStorage();
        ui.showTaskAdded(event, tasks.size());
    }

    private void handleOnCommand(String command) throws KiwiException {
        LocalDate targetDate = Parser.parseOnCommand(command);
        List<Task> tasksOnDate = tasks.getTasksOnDate(targetDate);
        String formattedDate = DateTimeParser.formatDate(targetDate);
        ui.showTasksOnDate(tasksOnDate, formattedDate);
    }

    /**
     * Saves tasks to storage.
     */
    private void saveTasksToStorage() {
        try {
            storage.saveTasks(tasks.getTasks());
        } catch (KiwiException e) {
            ui.showError("Warning: Could not save tasks - " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Kiwi("./data/kiwi.txt").run();
    }
}