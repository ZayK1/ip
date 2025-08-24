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
            } else if (line.equalsIgnoreCase("mark")) {

            } else {
                addTask(new Task(line), toDoList);
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
    public static void main(String[] args) {
        Kiwi kiwi = new Kiwi();
        kiwi.run();
    }
}
