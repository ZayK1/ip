public class Task {
    protected String description;
    protected boolean done = false;

    public Task(String description) {
        this.description = description;
    }

    public void mark() {
        this.done = true;
    }

    public void unmark() {
        this.done = false;
    }

    public boolean isDone() {
        return this.done;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return "[" + (this.done ? "X" : "") + "]:" + this.description;
    }
}