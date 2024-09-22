import java.util.HashMap;

public class EpicTask extends Task{
    HashMap<Long, SubTask>  subtasks = new HashMap<>();

    EpicTask(long id, String name, String description) {
        super(id, name, description);
    }

    public void addSubTask(SubTask subtask){
        subtask.setEpicTask(this);
        subtasks.put(subtask.getId(),subtask);
    }

    public void removeSubTask(SubTask subtask){
        subtasks.remove(subtask.getId());
    }
}
