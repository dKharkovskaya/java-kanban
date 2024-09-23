import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {
    static long common_id = 0;
    private final HashMap<Long, Task> tasks = new HashMap<>();
    private final HashMap<Long, EpicTask> epicTasks = new HashMap<>();

    public Task createTask(String name, String description) {
        common_id += 1;
        Task task = new Task(common_id, name, description);
        tasks.put(common_id, task);
        return task;
    }

    public EpicTask createEpicTask(String name, String description) {
        common_id += 1;
        EpicTask epic = new EpicTask(common_id, name, description);
        epicTasks.put(common_id, epic);
        return epic;
    }

    public SubTask createSubTask(EpicTask task, String name, String description) {
        if (epicTasks.containsKey(task.getId())) {
            common_id += 1;
            SubTask subTask = new SubTask(common_id, name, description);
            epicTasks.get(task.getId()).addSubTask(subTask);
            return subTask;
        }
        return null;
    }

    public ArrayList<Task> getListTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Task> getListEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    public ArrayList<Task> getListSubTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        for (EpicTask task : new ArrayList<>(epicTasks.values())) {
            tasks.addAll(task.getListSubTask());
        }
        return tasks;
    }

    public void clearListTasks() {
        tasks.clear();
    }

    public void clearListEpicTasks() {
        epicTasks.clear();
    }

    public Task updateTask(Task task, String name, String description) {
        if (tasks.containsKey(task.getId())) {
            Task newTask =  new Task(task.getId(), name, description);
            tasks.put(task.getId(),newTask);
            return newTask;
        }
        return null;
    }

    public EpicTask updateEpicTask(EpicTask task, String name, String description) {
        if (epicTasks.containsKey(task.getId())) {
            EpicTask newEpic = new EpicTask(task.getId(), name, description);

            for (SubTask sub : task.getListSubTask()) {
                newEpic.addSubTask(sub);
            }
            epicTasks.put(task.getId(), newEpic);
            return newEpic;
        }
        return null;
    }

    public SubTask updateSubTask(SubTask task, String name, String description) {
        if (epicTasks.containsKey(task.getEpicTask().getId())) {
            EpicTask epic = epicTasks.get(task.getEpicTask().getId());
            SubTask newSub = new SubTask(task.getId(), name, description);
            epic.removeSubTask(task);
            epic.addSubTask(newSub);
            return newSub;
        }
        return null;
    }

    public Task getTaskById(Long id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        }
        return null;
    }

    public EpicTask getEpicTaskById(Long id) {
        if (epicTasks.containsKey(id)) {
            return epicTasks.get(id);
        }
        return null;
    }

    public SubTask getSubTaskById(Long id) {
        for (EpicTask epic : epicTasks.values()) {
            for (SubTask sub : epic.getListSubTask()) {
                if (sub.getId() == id) {
                    return sub;
                }
            }
        }
        return null;
    }

    public Task deleteTaskById(Long id) {
        return tasks.remove(id);
    }

    public EpicTask deleteEpicTaskById(Long id) {
        return epicTasks.remove(id);
    }

    public SubTask deleteSubTaskById(Long id) {
        SubTask sub = getSubTaskById(id);
        if (sub == null) {
            return null;
        }

        EpicTask epic = sub.getEpicTask();

        return epic.removeSubTask(sub);
    }

    public Task deleteTask(Task task) {
        return deleteTaskById(task.getId());
    }

    public EpicTask deleteEpicTask(EpicTask task) {
        return deleteEpicTaskById(task.getId());
    }

    public SubTask deleteSubTask(SubTask task) {
        return deleteSubTaskById(task.getId());
    }

    public void moveTaskToProgress(Long id) {
        getTaskById(id).setStatus(Status.IN_PROGRESS);
    }

    public void moveSubTaskToProgress(Long id) {
        getSubTaskById(id).setStatus(Status.IN_PROGRESS);
    }

    public void moveTaskToDone(Long id) {
        getTaskById(id).setStatus(Status.DONE);
    }

    public void moveSubTaskToDone(Long id) {
        getSubTaskById(id).setStatus(Status.DONE);
    }
}
