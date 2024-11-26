package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
    protected final long id;
    protected final String name;
    protected final String description;
    protected Status status;
    protected TaskType taskType;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Task(long id, String name, String description, long duration, String startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.taskType = TaskType.TASK;
        this.startTime = LocalDateTime.parse(startTime, DATE_TIME_FORMATTER);
        this.duration = Duration.ofMinutes(duration);
        this.endTime = getEndTime();
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

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(Duration duration) {
        if (!duration.isNegative()) {
            this.duration = duration;
        }
    }

    public void setEndTime(LocalDateTime endTime) {
        if (this.startTime.isBefore(endTime)) {
            this.endTime = endTime;
        }
    }

    public LocalDateTime getEndTime() {
        return this.startTime.plusMinutes(duration.toMinutes());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task item = (Task) o;
        return id == item.id && Objects.equals(name, item.name) && Objects.equals(description, item.description);
    }

    @Override
    public String toString() {
        return getTaskType() + "@Id#" + id + ":" + "\n\tname: " + name + "\n\tdescription: " + description + "\n\tstatus: " + getStatus() + "\n\tStartTask: " + startTime.format(DATE_TIME_FORMATTER) + "\n\tEndTask: " + endTime.format(DATE_TIME_FORMATTER);
    }

}
