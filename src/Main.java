import manager.InMemoryTaskManager;
import task.*;

public class Main {

    public static void main(String[] args) {

        System.out.println("=========create========");

        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task1 = manager.createTask("task1", "description task1");
        Task task2 = manager.createTask("task2", "description task2");

        Epic epicTask1 = manager.createEpicTask("epicTask1", "description epicTask1");
        Subtask subTask1 = manager.createSubTask(epicTask1, "subTask1", "description subTask1");
        if (subTask1 == null) {
            return;
        }

        Epic epicTask2 = manager.createEpicTask("epicTask2", "description epicTask2");
        if (epicTask2 == null) {
            return;
        }
        Subtask subTask2 = manager.createSubTask(epicTask2, "subTask2", "description subTask2");
        if (subTask2 == null) {
            return;
        }
        Subtask subTask3 = manager.createSubTask(epicTask2, "subTask3", "description subTask3");
        if (subTask3 == null) {
            return;
        }

        printAllTasks(manager);

        System.out.println("=========update========");
        task2 = manager.updateTask(task2, "task3", "description task3");
        epicTask1 = manager.updateEpicTask(epicTask1, "epicTask3", "description epicTask3");
        subTask3 = manager.updateSubTask(subTask3, "subTask4", "description subTask4");

        printAllTasks(manager);

        System.out.print("\nTest getTaskById ... ");
        if (task2 != manager.getTaskById(task2.getId())) {
            return;
        }

        if (epicTask1 != manager.getEpicTaskById(epicTask1.getId())) {
            return;
        }

        if (subTask3 != manager.getSubTaskById(subTask3.getId())) {
            return;
        }
        System.out.println("success\n");

        System.out.println("========move to progress========");
        manager.moveTaskToProgress(task1.getId());
        manager.moveSubTaskToProgress(subTask1.getId());
        manager.moveSubTaskToProgress(subTask3.getId());

        printAllTasks(manager);

        System.out.println("========move to done========");
        manager.moveTaskToDone(task1.getId());
        manager.moveSubTaskToDone(subTask1.getId());
        manager.moveSubTaskToDone(subTask3.getId());

        printAllTasks(manager);

        System.out.println("========delete by id========");

        task2 = manager.deleteTaskById(task2.getId());
        epicTask1 = manager.deleteEpicTaskById(epicTask1.getId());
        //subTask3 = manager.deleteSubTaskById(subTask3.getId());

        printAllTasks(manager);

        System.out.println("========Clear List task.Task========");

        manager.clearListTasks();

        printAllTasks(manager);

        System.out.println("========Clear List EpicTask========");

        manager.clearListEpicTasks();

        printAllTasks(manager);

        System.out.println("========Print List History task========");
        printHistoryTask(manager);
    }

    static void printHistoryTask(InMemoryTaskManager manager) {
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

    static void printTasks(InMemoryTaskManager manager) {
        for (Task task : manager.getListTasks()) {
            System.out.println(task);
        }
    }

    static void printEpicTasks(InMemoryTaskManager manager) {
        for (Task task : manager.getListEpicTasks()) {
            System.out.println(task);
        }
    }

    static void printSubTasks(InMemoryTaskManager manager) {
        for (Task task : manager.getListSubTasks()) {
            System.out.println(task);
        }
    }

    static void printAllTasks(InMemoryTaskManager manager) {
        printTasks(manager);
        printEpicTasks(manager);
        printSubTasks(manager);
    }
}
