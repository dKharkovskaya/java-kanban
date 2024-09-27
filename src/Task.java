public class Task {
    protected final long id;
    protected final String name;
    protected final String description;
    protected Status status;
    protected String nameTaskType;

    Task(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        nameTaskType = String.valueOf(TaskType.TASK);
    }

    @Override
    public String toString() {
        return nameTaskType + "@Id#" + id + ":" + "\n\tname: " + name + "\n\tdescription: " + description + "\n\tstatus: " + getStatus();
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

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }
}
