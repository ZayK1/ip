package kiwi.task;
public abstract class Task {
    protected String description;
    protected boolean done = false;
    protected TaskType taskType;

    public Task(String description, TaskType taskType) {
        this.description = description;
        this.taskType = taskType;
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

    public TaskType getTaskType() {
        return this.taskType;
    }


    @Override
    public String toString() {
        return taskType.toString() + "[" + (this.done ? "X" : " ") + "] " + this.description;
    }
}