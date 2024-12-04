package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    private final HashMap<Long, Subtask> subTasks = new HashMap<>();
    private LocalDateTime endTime;

    public Epic(long id, String name, String description, long duration, String startTime) {
        super(id, name, description, duration, startTime);
        this.taskType = TaskType.EPIC;
    }

    public HashMap<Long, Subtask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(Subtask subtask) {
        subtask.setEpicTask(this);
        subTasks.put(subtask.getId(), subtask);
    }

    public Subtask removeSubTask(Subtask subtask) {
        return subTasks.remove(subtask.getId());
    }

    public ArrayList<Subtask> getListSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    public void updateStatus() {
        int[] arr = new int[Status.values().length];

        if (subTasks.isEmpty()) {
            setStatus(Status.NEW);
            return;
        }

        for (Task task : new ArrayList<Task>(subTasks.values())) {
            arr[task.getStatus().ordinal()] += 1;
        }

        if (arr[Status.NEW.ordinal()] == subTasks.size()) {
            setStatus(Status.NEW);
        } else if (arr[Status.DONE.ordinal()] == subTasks.size()) {
            setStatus(Status.DONE);
        } else {
            setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic item = (Epic) o;
        return id == item.id && Objects.equals(name, item.name) && Objects.equals(description, item.description);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    private void calculateStartTime() {
        LocalDateTime startSubtask = LocalDateTime.now();
        if (!subTasks.isEmpty()) {
            for (Subtask subtask : subTasks.values()) {
                if (subtask.getStartTime().isBefore(startSubtask)) {
                    startSubtask = subtask.getStartTime();
                }
            }
            this.startTime = startSubtask;
        }
    }

    private void calculateEndTime() {
        LocalDateTime endSubtask = startTime;
        if (subTasks.isEmpty()) {
            this.endTime = this.startTime.plusMinutes(duration.toMinutes());
        } else {
            for (Subtask subtask : subTasks.values()) {
                if (subtask.getEndTime().isAfter(endSubtask)) {
                    endSubtask = subtask.getEndTime();
                }
            }
            this.endTime = endSubtask;
        }
    }

    private void calculateDuration() {
        if (!subTasks.isEmpty()) {
            this.duration = Duration.between(this.startTime, this.endTime);
        }
    }

    public void calculateTime() {
        calculateStartTime();
        calculateEndTime();
        calculateDuration();

    }
}
