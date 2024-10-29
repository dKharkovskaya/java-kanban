package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import java.util.List;

public interface TaskManager {

    Task createTask(String name, String description);

    Epic createEpicTask(String name, String description);

    Subtask createSubTask(Epic task, String name, String description);

    List<Task> getListTasks();

    List<Task> getListEpicTasks();

    List<Task> getListSubTasks();

    Task getTaskById(Long id);

    Subtask getSubTaskById(Long id);

    Epic getEpicTaskById(Long id);

    Task updateTask(Task task, String name, String description);

    Epic updateEpicTask(Epic task, String name, String description);

    Subtask updateSubTask(Subtask task, String name, String description);

    Task deleteTask(Task task);

    Epic deleteEpicTask(Epic task);

    Subtask deleteSubTask(Subtask task);

    Task deleteTaskById(Long id);

    Epic deleteEpicTaskById(Long id);

    Subtask deleteSubTaskById(Long id);

    List<Task> getHistory();

    void clearListTasks();

    void clearListEpicTasks();

    void clearListSubTasks(Epic epic);
}
