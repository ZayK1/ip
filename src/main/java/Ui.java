import java.util.Scanner;

/**
 * Handles interactions with the user.
 */
public class Ui {
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome logo and message.
     */
    public void showWelcome() {
        showLine();
        String logo = " _  __  _  _      _  _ \n"
                + "| |/ / | || |    | || |\n"
                + "| ' /  | || | __ | || |\n"
                + "| . \\  | || |/ _\\| || |\n"
                + "|_|\\_\\ |_|||_\\__/|_||_|\n";
        System.out.println("Hello from\n" + logo);
        showLine();
        System.out.println("Hey, What can I do for you? \n");
        showLine();
    }

    /**
     * Displays the goodbye message.
     */
    public void showGoodbye() {
        System.out.println("Aight, see you again :)");
        showLine();
    }

    /**
     * Displays a horizontal line separator.
     */
    public void showLine() {
        System.out.println("___________________________________");
    }

    /**
     * Displays an error message.
     */
    public void showError(String message) {
        System.out.println("    ____________________________________________________________");
        System.out.println("     " + message);
        System.out.println("    ____________________________________________________________");
    }

    /**
     * Displays a loading error message.
     */
    public void showLoadingError() {
        showError("There was an error loading your tasks. So we're gonna have to start with a fresh list.");
    }

    /**
     * Displays a message to the user.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays a message with line separators.
     */
    public void showMessageWithLines(String message) {
        showLine();
        System.out.println(message);
        showLine();
    }

    /**
     * Displays the task list.
     */
    public void showTaskList(TaskList tasks) {
        System.out.println(tasks.toString());
        showLine();
    }

    /**
     * Displays confirmation for adding a task.
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
        showLine();
    }

    /**
     * Displays confirmation for deleting a task.
     */
    public void showTaskDeleted(Task task, int totalTasks) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
        showLine();
    }

    /**
     * Displays confirmation for marking a task as done.
     */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! Task done!");
        System.out.println(task);
        showLine();
    }

    /**
     * Displays confirmation for unmarking a task.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("Alright, Task has been marked undone");
        System.out.println(task);
        showLine();
    }

    /**
     * Displays tasks found on a specific date.
     */
    public void showTasksOnDate(java.util.List<Task> tasks, String formattedDate) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found on " + formattedDate + ".");
        } else {
            System.out.println("Here are the tasks on " + formattedDate + ":");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
        showLine();
    }

    /**
     * Reads a command from the user.
     */
    public String readCommand() {
        System.out.println("Your query: ");
        String command = scanner.nextLine();
        return command;
    }

    /**
     * Closes the scanner.
     */
    public void close() {
        scanner.close();
    }
}