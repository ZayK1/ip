package kiwi.task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;
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
        assert task != null : "Task cannot be null";
        this.tasks.add(task);
    }

    /**
     * Deletes a task from the list by index.
     */
    public Task deleteTask(int index) {
        assert isValidIndex(index) : "Index must be valid before deletion";
        return this.tasks.remove(index);
    }

    /**
     * Gets a task by index.
     */
    public Task getTask(int index) {
        assert isValidIndex(index) : "Index must be valid to get task";
        return this.tasks.get(index);
    }

    /**
     * Marks a task as done.
     */
    public void markTask(int index) {
        assert isValidIndex(index) : "Index must be valid to mark task";
        this.tasks.get(index).mark();
    }

    /**
     * Marks a task as not done.
     */
    public void unmarkTask(int index) {
        assert isValidIndex(index) : "Index must be valid to unmark task";
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
        return tasks.stream()
                .filter(task -> {
                    if (task instanceof Deadline) {
                        Deadline deadline = (Deadline) task;
                        return deadline.getDate() != null && deadline.getDate().equals(targetDate);
                    } else if (task instanceof Event) {
                        Event event = (Event) task;
                        java.time.LocalDate startDate = event.getStartDate();
                        java.time.LocalDate endDate = event.getEndDate();
                        
                        if (startDate != null && endDate != null) {
                            return !targetDate.isBefore(startDate) && !targetDate.isAfter(endDate);
                        } else if (startDate != null) {
                            return startDate.equals(targetDate);
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    /**
     * Returns tasks that contain the specified keyword in their description.
     */
    public List<Task> findTasks(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return tasks.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    /**
     * Sorts tasks chronologically by their dates (deadlines first, then events, then todos).
     */
    public void sortTasks() {
        tasks.sort(new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                // Get comparable dates for sorting
                java.time.LocalDate date1 = getTaskDate(t1);
                java.time.LocalDate date2 = getTaskDate(t2);
                
                // If both have dates, compare them
                if (date1 != null && date2 != null) {
                    return date1.compareTo(date2);
                }
                
                // Tasks with dates come before tasks without dates
                if (date1 != null && date2 == null) {
                    return -1;
                }
                if (date1 == null && date2 != null) {
                    return 1;
                }
                
                // If neither has dates, maintain original order
                return 0;
            }
            
            private java.time.LocalDate getTaskDate(Task task) {
                if (task instanceof Deadline) {
                    return ((Deadline) task).getDate();
                } else if (task instanceof Event) {
                    return ((Event) task).getStartDate();
                }
                return null; // Todo tasks have no date
            }
        });
    }

    @Override
    public String toString() {
        if (tasks.isEmpty()) {
            return "No tasks in your list.";
        }
        
        return "Here are the tasks in your list:\n" + 
               tasks.stream()
                    .map(task -> (tasks.indexOf(task) + 1) + "." + task + "\n")
                    .collect(Collectors.joining());
    }
}