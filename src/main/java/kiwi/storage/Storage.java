package kiwi.storage;

import kiwi.task.Task;
import kiwi.task.Deadline;
import kiwi.task.Event;
import kiwi.task.Todo;
import kiwi.exception.KiwiException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles loading and saving of tasks to/from file storage.
 */
public class Storage {
    private final String filePath;
    private final String directoryPath;

    public Storage(String filePath) {
        this.filePath = filePath;
        // Extract directory path from file path
        Path path = Paths.get(filePath);
        this.directoryPath = path.getParent() != null ? path.getParent().toString() : "";
    }

    /**
     * Loads tasks from file. Returns empty list if file doesn't exist or has errors.
     */
    public List<Task> loadTasks() throws KiwiException {
        List<Task> tasks = new ArrayList<>();

        try {
            createDirectoryIfNotExists();

            if (!Files.exists(Paths.get(filePath))) {
                return tasks;
            }

            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    Task task = parseTaskFromString(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                } catch (Exception e) {
                    System.out.println("Warning: Skipping corrupted line: " + line);
                }
            }
            reader.close();

        } catch (IOException e) {
            throw new KiwiException("Error loading tasks from file: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Saves tasks to file.
     */
    public void saveTasks(List<Task> tasks) throws KiwiException {
        try {
            createDirectoryIfNotExists();

            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            for (Task task : tasks) {
                writer.write(convertTaskToString(task));
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            throw new KiwiException("Error saving tasks to file: " + e.getMessage());
        }
    }

    /**
     * Creates directory if it doesn't exist.
     */
    private void createDirectoryIfNotExists() throws IOException {
        if (!directoryPath.isEmpty()) {
            Files.createDirectories(Paths.get(directoryPath));
        }
    }

    /**
     * Converts a task to string format for storage.
     * Format: TaskType | isDone | description | additionalInfo
     */
    private String convertTaskToString(Task task) {
        StringBuilder sb = new StringBuilder();

        sb.append(task.getTaskType().getSymbol());
        sb.append(" | ");

        sb.append(task.isDone() ? "1" : "0");
        sb.append(" | ");

        sb.append(task.getDescription());

        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            sb.append(" | ").append(deadline.getBy());
        } else if (task instanceof Event) {
            Event event = (Event) task;
            sb.append(" | ").append(event.getFrom()).append(" | ").append(event.getTo());
        }

        return sb.toString();
    }

    /**
     * Parses a string to create a Task object.
     */
    private Task parseTaskFromString(String line) {
        String[] parts = line.split(" \\| ");

        if (parts.length < 3) {
            return null;
        }

        String taskTypeSymbol = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();

        Task task;

        switch (taskTypeSymbol) {
            case "T":
                task = new Todo(description);
                break;

            case "D":
                if (parts.length < 4) return null;
                String by = parts[3].trim();
                task = new Deadline(description, by);
                break;

            case "E":
                if (parts.length < 5) return null;
                String from = parts[3].trim();
                String to = parts[4].trim();
                task = new Event(description, from, to);
                break;

            default:
                return null;
        }

        if (isDone) {
            task.mark();
        }

        return task;
    }
}