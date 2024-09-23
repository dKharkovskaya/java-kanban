import java.util.ArrayList;
import java.util.HashMap;

public class EpicTask extends Task {
    HashMap<Long, SubTask> subTasks = new HashMap<>();

    EpicTask(long id, String name, String description) {
        super(id, name, description);
        nameTaskType = "EpicTask";
    }

    public void addSubTask(SubTask subtask) {
        subtask.setEpicTask(this);
        subTasks.put(subtask.getId(), subtask);
    }

    public SubTask removeSubTask(SubTask subtask) {
        return subTasks.remove(subtask.getId());
    }

    public ArrayList<SubTask> getListSubTask() {
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
