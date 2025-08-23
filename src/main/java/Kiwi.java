import java.util.ArrayList;
import java.util.Scanner;



public class Kiwi {

    private final ArrayList<String> todoList = new ArrayList<>();
    private final Scanner input = new Scanner(System.in);

    public void run() {
        System.out.println("___________________________________");
        String logo = " _  __  _  _      _  _ \n"
                + "| |/ / | || |    | || |\n"
                + "| ' /  | || | __ | || |\n"
                + "| . \\  | || |/ _\\| || |\n"
                + "|_|\\_\\ |_|||_\\__/|_||_|\n";
        System.out.println("Hello from\n" + logo);
        System.out.println("___________________________________");
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
                System.out.println(displayTodoList(todoList));
                System.out.println("___________________________________");
            } else {
                addTask(line);
                System.out.println("___________________________________");
            }
        }
    }

    public void addTask(String task) {
        todoList.add(task);
        System.out.println("added: " + task);

    }

    public String displayTodoList(ArrayList<String> tasks) {
        if (tasks.isEmpty()) { return "No tasks left for today :)";}
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (String task : tasks) {
            count += 1;
            sb.append(count + ". " + task + "\n");
        }
        sb.append("Tasks left to complete: " + count);
        return sb.toString();
    }

    public static void main(String[] args) {
        Kiwi kiwi = new Kiwi();
        kiwi.run();
    }
}
