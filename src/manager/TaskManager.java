package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;

public interface TaskManager {
    Task createTask(String name, String description);
    Epic createEpicTask(String name, String description);
    Subtask createSubTask(Epic task, String name, String description);
    ArrayList<Task> getListTasks();
    ArrayList<Task> getListEpicTasks();
    ArrayList<Task> getListSubTasks();
    void clearListTasks();
    void clearListEpicTasks();
}
