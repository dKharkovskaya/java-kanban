package task;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<Long, Subtask> subTasks = new HashMap<>();

    public Epic(long id, String name, String description) {
        super(id, name, description);
        nameTaskType = String.valueOf(TaskType.EPIC);
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
}
