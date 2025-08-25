public class Deadline extends Task {
    private String by;

    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    @Override
    public String toString() {
        return taskType.toString() + "[" + (this.done ? "X" : " ") + "] " + this.description + " (by: " + this.by + ")";
    }
}