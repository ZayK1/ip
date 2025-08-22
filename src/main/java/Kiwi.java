import java.util.Scanner;

public class Kiwi {
    public static void main(String[] args) {
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

        Scanner input = new Scanner(System.in);

        boolean end = false;
        while (!end) {
            System.out.println("Your query: ");
            String line = input.nextLine();
            System.out.println("___________________________________");
            if (line.equalsIgnoreCase("bye")) {
                end = true;
                System.out.println("Bye, see you again :)");
                System.out.println("___________________________________");
                break;
            } else {
                System.out.println(line);
                System.out.println("___________________________________");
            }
        }
    }
}
