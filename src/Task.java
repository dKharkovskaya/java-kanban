import java.util.HashMap;

public class Task {
    private long id;
    private final String name;
    private final String description;
    private Status status;

    Task(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    @Override
    public String toString(){
        return "Task " + id + ":" + "\n\tname: " + name + "\n\tdescription: " + description + "\n\tstatus: " + status;
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
