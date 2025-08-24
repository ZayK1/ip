import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> todoList = new ArrayList<>();

    public void add(Task task) {
        this.todoList.add(task);
        System.out.println("added: " + task.getDescription());
    }

    public int size() {
        return this.todoList.size();
    }

    public Task get(int index) {
        return this.todoList.get(index);
    }

    public void mark(int index) {
        this.todoList.get(index).mark();
    }

    public void unmark(int index) {
        this.todoList.get(index).unmark();
    }

    @Override
    public String toString() {
        if (todoList.isEmpty()) { return "No tasks left for today :)";}
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (Task task : todoList) {
            count += 1;
            sb.append(count + ". " + task + "\n");
        }
        sb.append("Tasks left to complete: " + count);
        return sb.toString();

    }
}