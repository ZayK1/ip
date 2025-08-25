import java.awt.image.TileObserver;
import java.util.ArrayList;
import java.util.Scanner;



public class Kiwi {
    private final Scanner input = new Scanner(System.in);
    private final TaskList toDoList =  new TaskList();

    public void run() {
        displayLogo();
        System.out.println("What can I do for you? \n");
        System.out.println("___________________________________");


        while (true) {
            System.out.println("Your query: ");
            String line = input.nextLine();
            System.out.println("___________________________________");
            if (line.equalsIgnoreCase("bye")) {
                System.out.println("Bye, see you again :)");
                System.out.println("___________________________________");
                break;
            } else if (line.equalsIgnoreCase("List")) {
                System.out.println(displayTodoList(toDoList));
                System.out.println("___________________________________");
            } else if (isMarkCommand(line)) {
                int taskId = Integer.parseInt(line.split(" ")[1]);
                mark(taskId, toDoList);
            } else if (isUnmarkCommand(line)) {
                int taskId = Integer.parseInt(line.split(" ")[1]);
                unmark(taskId, toDoList);
            } else if (line.startsWith("todo")) {
                handleTodoCommand(line);
            } else if (line.startsWith("deadline")) {
                handleDeadlineCommand(line);
            } else if (line.startsWith("event")) {
                handleEventCommand(line);
            } else {
                addTask(new Todo(line), toDoList);
                System.out.println("___________________________________");
            }
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

    public void handleTodoCommand(String line) {
        String description = line.substring(5);
        Todo todo = new Todo(description);
        addTask(todo, toDoList);
        System.out.println("___________________________________");
    }

    public void handleDeadlineCommand(String line) {
        try {
            String[] parts =  line.substring(9).split(" /by ", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid deadline format. Use: deadline <description> /by <time>");
            }
            String description = parts[0];
            String deadline = parts[1];
            Deadline task = new Deadline(description, deadline);
            addTask(task, toDoList);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("___________________________________");
    }

    public void handleEventCommand(String line) {
        try {
            String[] firstSplit = line.substring(6).split(" /from ", 2);
            if (firstSplit.length != 2) {
                throw new IllegalArgumentException("Invalid event format. Use: event <description> /from <start> /to <end>");
            }
            String description = firstSplit[0];
            String[] timeParts = firstSplit[1].split(" /to ", 2);
            if (timeParts.length != 2) {
                throw new IllegalArgumentException("Invalid event format. Use: event <description> /from <start> /to <end>");
            }
            String from = timeParts[0];
            String to = timeParts[1];
            Event event = new Event(description, from, to);

            addTask(event, toDoList);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("___________________________________");
    }


    public static void main(String[] args) {
        Kiwi kiwi = new Kiwi();
        kiwi.run();
    }
}
