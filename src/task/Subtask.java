package task;

import java.util.Objects;

public class Subtask extends Task {
    private Epic epicTask;

    public Subtask(long id, String name, String description) {
        super(id, name, description);
        this.taskType = TaskType.SUBTASK;
    }

    public Epic getEpicTask() {
        return epicTask;
    }

    public void setEpicTask(Epic epicTask) {
        this.epicTask = epicTask;
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
        epicTask.updateStatus();
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + "\towned: EpicTask Id#" + epicTask.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask item = (Subtask) o;
        return id == item.id && Objects.equals(name, item.name) && Objects.equals(description, item.description);
    }
}
