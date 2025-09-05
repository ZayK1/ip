package kiwi.task;

import java.util.ArrayList;
import java.util.List;
/**
 * Manages a list of tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    public TaskList() {
    }

    public TaskList(List<Task> tasks) {
        this.tasks.addAll(tasks);
    }

    /**
     * Adds a task to the list.
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Deletes a task from the list by index.
     */
    public Task deleteTask(int index) {
        return this.tasks.remove(index);
    }

    /**
     * Gets a task by index.
     */
    public Task getTask(int index) {
        return this.tasks.get(index);
    }

    /**
     * Marks a task as done.
     */
    public void markTask(int index) {
        this.tasks.get(index).mark();
    }

    /**
     * Marks a task as not done.
     */
    public void unmarkTask(int index) {
        this.tasks.get(index).unmark();
    }

    /**
     * Returns the number of tasks.
     */
    public int size() {
        return this.tasks.size();
    }

    /**
     * Returns all tasks as a list.
     */
    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Checks if the task index is valid.
     */
    public boolean isValidIndex(int index) {
        return index >= 0 && index < tasks.size();
    }

    /**
     * Returns tasks that occur on a specific date.
     */
    public List<Task> getTasksOnDate(java.time.LocalDate targetDate) {
        List<Task> tasksOnDate = new ArrayList<>();

        for (Task task : tasks) {
            if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                if (deadline.getDate() != null && deadline.getDate().equals(targetDate)) {
                    tasksOnDate.add(task);
                }
            } else if (task instanceof Event) {
                Event event = (Event) task;
                java.time.LocalDate startDate = event.getStartDate();
                java.time.LocalDate endDate = event.getEndDate();

                // Check if target date falls within event date range
                if (startDate != null && endDate != null) {
                    if (!targetDate.isBefore(startDate) && !targetDate.isAfter(endDate)) {
                        tasksOnDate.add(task);
                    }
                } else if (startDate != null && startDate.equals(targetDate)) {
                    tasksOnDate.add(task);
                }
            }
        }

        return tasksOnDate;
    }

    /**
     * Returns tasks that contain the specified keyword in their description.
     */
    public List<Task> findTasks(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerKeyword)) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks;
    }

    @Override
    public String toString() {
        if (tasks.isEmpty()) {
            return "No tasks in your list.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append((i + 1) + "." + tasks.get(i) + "\n");
        }
        return sb.toString();
    }
}