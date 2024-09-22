import java.util.ArrayList;
import java.util.HashMap;


public class Manager {
    static long common_id = 0;
    HashMap<Long, Task> tasks = new HashMap<>();
    HashMap<Long, EpicTask> epicTasks = new HashMap<>();

    public void addTask(String name, String description) {
        common_id += 1;
        tasks.put(common_id,  new Task(common_id, name, description));
    }

    public void addEpicTask(String name, String description) {
        common_id += 1;
        epicTasks.put(common_id, new EpicTask(common_id, name, description));
    }

    public void addSubTask(EpicTask epicTask, String name, String description) {
        common_id += 1;
        epicTask.addSubTask(new SubTask(common_id, name, description));
    }

    public ArrayList <Task> getListTasks(){
        return new ArrayList<>(tasks.values());
    }

    public ArrayList <EpicTask> getListEpicTasks(){
        return new ArrayList<>(epicTasks.values());
    }

    public void clearListTasks() {
        tasks.clear();
    }

    public Task getTaskById(Long id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        }
        return null;
    }

//    public void updateTask (Task task) {
//        if (tasks.containsKey(task.getId())) {
//            deleteTaskById(task.getId());
//        }
//        addTask(task);
//    }
    public void deleteTaskById(Long id) {
        tasks.remove(id);
    }

}
