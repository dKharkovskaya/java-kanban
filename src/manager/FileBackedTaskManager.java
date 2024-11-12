package manager;

import task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private String fileName;
    private static final String HEAD_CSV = "id,type,name,status,description,epic";

    public FileBackedTaskManager(String fileName) {
        this.fileName = fileName;
    }

    private void save() {
        try (Writer fileWriter = new FileWriter(this.fileName)) {
            fileWriter.write(HEAD_CSV);
            fileWriter.write("\n");
            for (Long id : tasks.keySet()) {
                fileWriter.write(toString(tasks.get(id)));
                fileWriter.write("\n");
            }
            for (Long id : epicTasks.keySet()) {
                fileWriter.write(toString(epicTasks.get(id)));
                fileWriter.write("\n");
            }
            for (Task subtask : getListSubTasks()) {
                fileWriter.write(toString(subtask));
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время записи файла.");
        }
    }

    private String toString(Task task) {
        String epicId = "";
        if (task.getNameTaskType().equals(String.valueOf(TaskType.SUBTASK))) {
            Subtask sbTask = (Subtask) task;
            epicId = String.valueOf(sbTask.getEpicTask().getId());
        }
        return task.getId() + "," + task.getNameTaskType() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "," + epicId;
    }

    public static void loadFromFile(String file) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (fileReader.ready()) {
                String line = fileReader.readLine();
                if (line.isEmpty()) {
                    return;
                }
                Task taskFromFile = fromString(line);
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


    private static Task fromString(String line) {
        String[] arrParam = line.split(",");
        for (int i = 0; i < arrParam.length; i++) {
            if (arrParam[1].equals("TASK")) {
                Task task = new Task(Long.parseLong(arrParam[0]), arrParam[2], arrParam[4]);
                task.setStatus(Status.valueOf(arrParam[3]));
                return task;
            } else if (arrParam[1].equals("EPIC")) {
                Epic epic = new Epic(Long.parseLong(arrParam[0]), arrParam[2], arrParam[4]);
                epic.setStatus(Status.valueOf(arrParam[3]));
                return epic;
            } else if (arrParam[1].equals("SUBTASK")) {
                Subtask subtask = new Subtask(Long.parseLong(arrParam[0]), arrParam[2], arrParam[4]);
                try {
                    subtask.setEpicTask(epicTasks.get(Long.parseLong(arrParam[5])));
                } catch (NumberFormatException e) {
                    System.out.println("Эпик с id = " + arrParam[5] + " ещё не был добавлен. Проверьте формат csv файла");
                    throw new RuntimeException(e);
                }
                subtask.setStatus(Status.valueOf(arrParam[3]));
                return subtask;

            }
        }
        return null;
    }

    @Override
    public Task createTask(String name, String description) {
        Task task = super.createTask(name, description);
        save();
        return task;
    }

    @Override
    public Epic createEpicTask(String name, String description) {
        Epic task = super.createEpicTask(name, description);
        save();
        return task;
    }

    @Override
    public Subtask createSubTask(Epic task, String name, String description) {
        Subtask subTask = super.createSubTask(task, name, description);
        save();
        return subTask;
    }

    @Override
    public Task updateTask(Task task, String name, String description) {
        Task updatingTask = super.updateTask(task, name, description);
        save();
        return updatingTask;
    }

    @Override
    public Epic updateEpicTask(Epic task, String name, String description) {
        Epic updatingTask = super.updateEpicTask(task, name, description);
        save();
        return updatingTask;
    }

    @Override
    public Subtask updateSubTask(Subtask task, String name, String description) {
        Subtask updatingTask = super.updateSubTask(task, name, description);
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
