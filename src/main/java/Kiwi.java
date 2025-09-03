import java.util.Scanner;

public class Kiwi {
    private final Scanner input = new Scanner(System.in);
    private final TaskList toDoList;

    public Kiwi() {
        // Initialize storage with file path
        Storage storage = new Storage("./data/kiwi.txt");
        this.toDoList = new TaskList(storage);
    }

    public void run() {
        displayLogo();
        System.out.println("What can I do for you? \n");
        System.out.println("___________________________________");

        while (true) {
            System.out.println("Your query: ");
            String line = input.nextLine();
            System.out.println("___________________________________");

            try {
                if (line.equalsIgnoreCase("bye")) {
                    System.out.println("Bye, see you again :)");
                    System.out.println("___________________________________");
                    break;
                } else if (line.equalsIgnoreCase("list")) {
                    System.out.println(displayTodoList(toDoList));
                    System.out.println("___________________________________");
                } else if (isMarkCommand(line)) {
                    handleMarkCommand(line);
                } else if (isUnmarkCommand(line)) {
                    handleUnmarkCommand(line);
                } else if (isDeleteCommand(line)) {
                    handleDeleteCommand(line);
                } else if (line.startsWith("todo")) {
                    handleTodoCommand(line);
                } else if (line.startsWith("deadline")) {
                    handleDeadlineCommand(line);
                } else if (line.startsWith("event")) {
                    handleEventCommand(line);
                } else {
                    throw new UnknownCommandException();
                }
            } catch (KiwiException e) {
                System.out.println("    ____________________________________________________________");
                System.out.println("     " + e.getMessage());
                System.out.println("    ____________________________________________________________");
            } catch (Exception e) {
                System.out.println("    ____________________________________________________________");
                System.out.println("     Something unexpected happened: " + e.getMessage());
                System.out.println("    ____________________________________________________________");
            }
        }
    }

    public boolean isDeleteCommand(String command) {
        return command.startsWith("delete ");
    }

    public void handleDeleteCommand(String line) throws KiwiException {
        try {
            int taskId = Integer.parseInt(line.split(" ")[1]);
            if (taskId < 1 || taskId > toDoList.size()) {
                throw new KiwiException("Task number " + taskId + " doesn't exist!");
            }

            Task deletedTask = toDoList.get(taskId - 1);
            toDoList.delete(taskId - 1);

            System.out.println("Noted. I've removed this task:");
            System.out.println("  " + deletedTask);
            System.out.println("Now you have " + toDoList.size() + " tasks in the list.");
            System.out.println("___________________________________");

        } catch (NumberFormatException e) {
            throw new KiwiException("Please provide a valid task number to delete!");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new KiwiException("Please specify which task to delete!");
        }
    }

    public void displayLogo() {
        System.out.println("___________________________________");
        String logo = " _  __  _  _      _  _ \n"
                + "| |/ / | || |    | || |\n"
                + "| ' /  | || | __ | || |\n"
                + "| . \\  | || |/ _\\| || |\n"
                + "|_|\\_\\ |_|||_\\__/|_||_|\n";
        System.out.println("Hello from\n" + logo);
        System.out.println("___________________________________");
    }

    public String displayTodoList(TaskList list) {
        return list.toString();
    }

    public void addTask(Task task, TaskList list) {
        list.add(task);
    }

    public void mark(int taskId, TaskList list) {
        list.mark(taskId - 1);
        System.out.println("Nice! Task done!");
        System.out.println(list.get(taskId - 1));
        System.out.println("___________________________________");
    }

    public void unmark(int taskId, TaskList list) {
        list.unmark(taskId - 1);
        System.out.println("Alright, Task has been marked undone");
        System.out.println(list.get(taskId - 1));
        System.out.println("___________________________________");
    }

    public boolean isMarkCommand(String command) {
        return command.startsWith("mark ");
    }

    public boolean isUnmarkCommand(String command) {
        return command.startsWith("unmark ");
    }

    public void handleTodoCommand(String line) throws KiwiException {
        if (line.trim().equals("todo") || line.length() <= 4 || line.substring(4).trim().isEmpty()) {
            throw new EmptyDescriptionException("todo");
        }
        String description = line.substring(5);
        Todo todo = new Todo(description);
        addTask(todo, toDoList);
        System.out.println("___________________________________");
    }

    public void handleDeadlineCommand(String line) throws KiwiException {
        if (line.trim().equals("deadline")) {
            throw new EmptyDescriptionException("deadline");
        }

        String withoutCommand = line.substring(9);
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

        Deadline task = new Deadline(description, deadline);
        addTask(task, toDoList);
        System.out.println("___________________________________");
    }

    public void handleEventCommand(String line) throws KiwiException {
        if (line.trim().equals("event")) {
            throw new EmptyDescriptionException("event");
        }

        String withoutCommand = line.substring(6);
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

        Event event = new Event(description, from, to);
        addTask(event, toDoList);
        System.out.println("___________________________________");
    }

    public void handleMarkCommand(String line) throws KiwiException {
        try {
            int taskId = Integer.parseInt(line.split(" ")[1]);
            if (taskId < 1 || taskId > toDoList.size()) {
                throw new KiwiException("Task number " + taskId + " doesn't exist");
            }
            mark(taskId, toDoList);
        } catch (NumberFormatException e) {
            throw new KiwiException("Please provide a valid task number to mark");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new KiwiException("Please specify which task to mark");
        }
    }

    public void handleUnmarkCommand(String line) throws KiwiException {
        try {
            int taskId = Integer.parseInt(line.split(" ")[1]);
            if (taskId < 1 || taskId > toDoList.size()) {
                throw new KiwiException("Task number " + taskId + " doesn't exist");
            }
            unmark(taskId, toDoList);
        } catch (NumberFormatException e) {
            throw new KiwiException("Please provide a valid task number to unmark");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new KiwiException("Please specify which task to unmark");
        }
    }

    public static void main(String[] args) {
        Kiwi kiwi = new Kiwi();
        kiwi.run();
    }
}