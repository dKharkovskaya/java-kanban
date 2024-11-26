package manager;

import task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private long id = 0;
    protected static final HashMap<Long, Task> tasks = new HashMap<>();
    protected static final HashMap<Long, Epic> epicTasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected Set<Task> priorityTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task createTask(String name, String description, long duration, String startTime) {
        id += 1;
        Task task = new Task(id, name, description, duration, startTime);
        tasks.put(id, task);
        addPriorityTask(task);
        return task;
    }

    @Override
    public Epic createEpicTask(String name, String description, long duration, String startTime) {
        id += 1;
        Epic epic = new Epic(id, name, description, duration, startTime);
        epicTasks.put(id, epic);
        return epic;
    }

    @Override
    public Subtask createSubTask(Epic task, String name, String description, long duration, String startTime) {
        if (epicTasks.containsKey(task.getId())) {
            id += 1;
            Subtask subTask = new Subtask(id, name, description, duration, startTime);
            epicTasks.get(task.getId()).addSubTask(subTask);
            addPriorityTask(subTask);
            return subTask;
        }
        return null;
    }

    @Override
    public List<Task> getListTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> getListEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public List<Task> getListSubTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        for (Epic task : new ArrayList<>(epicTasks.values())) {
            tasks.addAll(task.getListSubTask());
        }
        return tasks;
    }

    @Override
    public Task updateTask(Task task, String name, String description, long duration, String startTime) {
        if (tasks.containsKey(task.getId())) {
            Task newTask = new Task(task.getId(), name, description, duration, startTime);
            tasks.put(task.getId(), newTask);
            return newTask;
        }
        return null;
    }

    @Override
    public Epic updateEpicTask(Epic task, String name, String description, long duration, String startTime) {
        if (epicTasks.containsKey(task.getId())) {
            Epic newEpic = new Epic(task.getId(), name, description, duration, startTime);

            for (Subtask sub : task.getListSubTask()) {
                newEpic.addSubTask(sub);
            }
            epicTasks.put(task.getId(), newEpic);
            return newEpic;
        }
        return null;
    }

    @Override
    public Subtask updateSubTask(Subtask task, String name, String description, long duration, String startTime) {
        if (epicTasks.containsKey(task.getEpicTask().getId())) {
            Epic epic = epicTasks.get(task.getEpicTask().getId());
            Subtask newSub = new Subtask(task.getId(), name, description, duration, startTime);
            epic.removeSubTask(task);
            epic.addSubTask(newSub);
            return newSub;
        }
        return null;
    }

    @Override
    public Task getTaskById(Long id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicTaskById(Long id) {
        historyManager.add(epicTasks.get(id));
        return epicTasks.get(id);
    }

    @Override
    public Subtask getSubTaskById(Long id) {
        for (Epic epic : epicTasks.values()) {
            for (Subtask sub : epic.getListSubTask()) {
                if (sub.getId() == id) {
                    historyManager.add(sub);
                    return sub;
                }
            }
        }
        return null;
    }

    @Override
    public Task deleteTaskById(Long id) {
        return tasks.remove(id);
    }

    @Override
    public Epic deleteEpicTaskById(Long id) {
        return epicTasks.remove(id);
    }

    @Override
    public Subtask deleteSubTaskById(Long id) {
        Subtask sub = getSubTaskById(id);
        if (sub == null) {
            return null;
        }

        Epic epic = sub.getEpicTask();

        return epic.removeSubTask(sub);
    }

    @Override
    public Task deleteTask(Task task) {
        return deleteTaskById(task.getId());
    }

    @Override
    public Epic deleteEpicTask(Epic task) {
        return deleteEpicTaskById(task.getId());
    }

    @Override
    public Subtask deleteSubTask(Subtask task) {
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

    @Override
    public void clearListTasks() {
        tasks.clear();
    }

    @Override
    public void clearListEpicTasks() {
        epicTasks.clear();
    }

    @Override
    public void clearListSubTasks(Epic epic) {
        epic.getListSubTask().clear();
    }

    public void addPriorityTask(Task task) {
        priorityTasks.add(task);
        checkIntersectionTask();
    }

    public List<Task> getPrioritizedTasks() {
        return priorityTasks.stream().toList();
    }

    public void checkIntersectionTask() {
        List<Task> tasks = getPrioritizedTasks();
        for (int i = 0; i < tasks.size(); i++) {
            for (int j = 0; j < tasks.size(); j++) {
                if (tasks.get(i).equals(tasks.get(j))) {
                    continue;
                }
                if (tasks.get(i).getStartTime() != null && tasks.get(i).getEndTime() != null
                        && tasks.get(j).getStartTime() != null && tasks.get(j).getEndTime() != null) {
                    //Проверка на пересечение
                    if (tasks.get(i).getStartTime().equals(tasks.get(j).getStartTime())
                            || tasks.get(i).getEndTime().equals(tasks.get(j).getEndTime())
                            || (tasks.get(i).getStartTime().isAfter(tasks.get(j).getStartTime()) && tasks.get(i).getStartTime().isBefore(tasks.get(j).getEndTime()))
                            || (tasks.get(i).getEndTime().isAfter(tasks.get(j).getStartTime()) && tasks.get(i).getEndTime().isBefore(tasks.get(j).getEndTime()))
                            || (tasks.get(j).getStartTime().isAfter(tasks.get(i).getStartTime()) && tasks.get(j).getEndTime().isBefore(tasks.get(i).getEndTime()))
                            || (tasks.get(i).getStartTime().isAfter(tasks.get(j).getStartTime()) && tasks.get(i).getEndTime().isBefore(tasks.get(j).getEndTime()))
                    ) {
                        System.out.println("Задача с id=" + tasks.get(i).getId() + " пересекается с задачей id=" + tasks.get(j).getId());
                    }

                }
            }
        }
    }

}
