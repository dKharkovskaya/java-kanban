package manager;

import task.*;
import util.FormatterUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

import static util.FormatterUtil.fromString;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final String fileName;
    private static final String HEAD_CSV = "id,type,name,status,description,epic,duration (minutes),startTime, endTime";

    public FileBackedTaskManager(String fileName) {
        this.fileName = fileName;
    }

    private void save() {
        try (Writer fileWriter = new FileWriter(this.fileName)) {
            fileWriter.write(HEAD_CSV);
            fileWriter.write("\n");
            for (Long id : tasks.keySet()) {
                fileWriter.write(FormatterUtil.toString(tasks.get(id)));
                fileWriter.write("\n");
            }
            for (Long id : epicTasks.keySet()) {
                fileWriter.write(FormatterUtil.toString(epicTasks.get(id)));
                fileWriter.write("\n");
            }
            for (Task subtask : getListSubTasks()) {
                fileWriter.write(FormatterUtil.toString(subtask));
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время записи файла.");
        }
    }

    public static void loadFromFile(String file) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (fileReader.ready()) {
                String line = fileReader.readLine();
                if (line.isEmpty()) {
                    return;
                }
                Task taskFromFile = fromString(line, epicTasks);
                if (taskFromFile == null) {
                    continue;
                }
                if (taskFromFile instanceof Epic epic) {
                    epicTasks.put(taskFromFile.getId(), epic);
                } else if (taskFromFile instanceof Subtask subtask) {
                    epicTasks.get(subtask.getEpicTask().getId()).addSubTask(subtask);
                } else {
                    tasks.put(taskFromFile.getId(), taskFromFile);
                }
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }
    }

    @Override
    public Task createTask(String name, String description, long duration, String startTime) {
        Task task = super.createTask(name, description, duration, startTime);
        save();
        return task;
    }

    @Override
    public Epic createEpicTask(String name, String description, long duration, String startTime) {
        Epic task = super.createEpicTask(name, description, duration, startTime);
        save();
        return task;
    }

    @Override
    public Subtask createSubTask(Epic task, String name, String description, long duration, String startTime) {
        Subtask subTask = super.createSubTask(task, name, description, duration, startTime);
        save();
        return subTask;
    }

    @Override
    public Task updateTask(Task task, String name, String description, long duration, String startTime) {
        Task updatingTask = super.updateTask(task, name, description, duration, startTime);
        save();
        return updatingTask;
    }

    @Override
    public Epic updateEpicTask(Epic task, String name, String description, long duration, String startTime) {
        Epic updatingTask = super.updateEpicTask(task, name, description, duration, startTime);
        save();
        return updatingTask;
    }

    @Override
    public Subtask updateSubTask(Subtask task, String name, String description, long duration, String startTime) {
        Subtask updatingTask = super.updateSubTask(task, name, description, duration, startTime);
        save();
        return updatingTask;
    }

    @Override
    public Task deleteTaskById(Long id) {
        save();
        return tasks.remove(id);
    }

    @Override
    public Epic deleteEpicTaskById(Long id) {
        Epic epic = super.deleteEpicTaskById(id);
        save();
        return epic;
    }

    @Override
    public Subtask deleteSubTaskById(Long id) {
        Subtask sb = super.deleteSubTaskById(id);
        save();
        return sb;
    }

    @Override
    public Task deleteTask(Task task) {
        Task deletedTask = super.deleteTask(task);
        save();
        return deletedTask;
    }

    @Override
    public Epic deleteEpicTask(Epic task) {
        Epic epic = super.deleteEpicTask(task);
        save();
        return epic;
    }

    @Override
    public Subtask deleteSubTask(Subtask task) {
        Subtask subtask = super.deleteSubTask(task);
        save();
        return subtask;
    }

    @Override
    public void clearListTasks() {
        super.clearListTasks();
        save();
    }

    @Override
    public void clearListEpicTasks() {
        super.clearListEpicTasks();
        save();
    }

    @Override
    public void clearListSubTasks(Epic epic) {
        super.clearListSubTasks(epic);
        save();
    }

}
