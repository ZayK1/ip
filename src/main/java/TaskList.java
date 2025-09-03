import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final ArrayList<Task> todoList = new ArrayList<>();
    private final Storage storage;

    public TaskList(Storage storage) {
        this.storage = storage;
        loadTasksFromStorage();
    }

    /**
     * Loads tasks from storage on initialization.
     */
    private void loadTasksFromStorage() {
        try {
            List<Task> loadedTasks = storage.loadTasks();
            todoList.addAll(loadedTasks);
        } catch (KiwiException e) {
            System.out.println("Warning: " + e.getMessage());
            System.out.println("Starting with empty task list.");
        }
    }

    /**
     * Saves tasks to storage.
     */
    private void saveTasksToStorage() {
        try {
            storage.saveTasks(todoList);
        } catch (KiwiException e) {
            System.out.println("Warning: Could not save tasks - " + e.getMessage());
        }
    }

    public void add(Task task) {
        this.todoList.add(task);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + this.todoList.size() + " tasks in the list.");
        saveTasksToStorage(); // Auto-save after adding
    }

    public void delete(int index) {
        this.todoList.remove(index);
        saveTasksToStorage(); // Auto-save after deleting
    }

    public int size() {
        return this.todoList.size();
    }

    public Task get(int index) {
        return this.todoList.get(index);
    }

    public void mark(int index) {
        this.todoList.get(index).mark();
        saveTasksToStorage(); // Auto-save after marking
    }

    public void unmark(int index) {
        this.todoList.get(index).unmark();
        saveTasksToStorage(); // Auto-save after unmarking
    }

    @Override
    public String toString() {
        if (todoList.isEmpty()) {
            return "No tasks in your list.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
        for (int i = 0; i < todoList.size(); i++) {
            sb.append((i + 1) + "." + todoList.get(i) + "\n");
        }
        return sb.toString();
    }
}