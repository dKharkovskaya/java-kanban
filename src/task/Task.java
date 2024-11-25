package task;

import java.util.Objects;

public class Task {
    protected final long id;
    protected final String name;
    protected final String description;
    protected Status status;
    protected TaskType taskType;

    public Task(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.taskType = TaskType.TASK;
    }

    @Override
    public String toString() {
        return getTaskType() + "@Id#" + id + ":" + "\n\tname: " + name + "\n\tdescription: " + description + "\n\tstatus: " + getStatus();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public TaskType getTaskType() {
        return taskType;
    }


    public void setStatus(Status status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task item = (Task) o;
        return id == item.id && Objects.equals(name, item.name) && Objects.equals(description, item.description);
    }
}
