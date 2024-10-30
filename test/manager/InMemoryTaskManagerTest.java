package manager;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest {
    private static final InMemoryTaskManager manager = new InMemoryTaskManager();
    static Task task1;
    static Epic epicTask1;
    static Subtask subtask1;

    @BeforeAll
    static void beforeAll() {
        task1 = manager.createTask("Task1", "Description_1");
        epicTask1 = manager.createEpicTask("epicTask1", "Description_1");
        subtask1 = manager.createSubTask(epicTask1, "subTask1", "Description1");
    }

    @Test
    void shouldIncrementIdAfterCreateTask() {
        assertEquals(1, manager.tasks.get(task1.getId()).getId());
    }

    @Test
    public void shouldIncrementIdAfterCreateEpicTask() {
        assertEquals(2, manager.epicTasks.get(epicTask1.getId()).getId());
    }

    @Test
    public void shouldReturnTask1EqualsTask2() {
        Task task2 = new Task(1, "Task1", "Description_1");
        assertEquals(task1, task2);
    }

    @Test
    public void shouldReturnEpicTask1EqualsEpicTask2() {
        Epic epicTask2 = new Epic(2, "epicTask1", "Description_1");
        assertEquals(epicTask1, epicTask2);
    }

    @Test
    public void shouldBeNotNullEpicTaskInSubtask() {
        assertNotNull(subtask1.getEpicTask());
    }

    @Test
    public void shouldReturnSubTask1EqualsSubTask1() {
        Subtask subTask2 = new Subtask(3, "subTask1", "Description1");
        subTask2.setEpicTask(new Epic(2, "epicTask1", "Description_1"));
        assertEquals(epicTask1.getListSubTask().getFirst(), subTask2);
    }

    @Test
    public void should1TaskInTasks() {
        assertEquals(1, manager.getListTasks().size());
    }

    @Test
    public void should1EpicTaskInTasks() {
        assertEquals(1, manager.getListEpicTasks().size());
    }

    @Test
    public void should1SubTaskInSubTasks() {
        assertEquals(1, manager.getListSubTasks().size());
    }

    @Test
    public void shouldUpdateTask() {
        manager.updateTask(task1, "new Task1", "Desription2");
        assertEquals(1, manager.getTaskById(task1.getId()).getId());
        assertEquals("new Task1", manager.getTaskById(task1.getId()).getName());
        assertEquals("Desription2", manager.getTaskById(task1.getId()).getDescription());
    }

    @Test
    public void shouldUpdateEpic() {
        manager.updateEpicTask(epicTask1, "new EpicTask1", "Desription2");
        assertEquals(2, manager.getEpicTaskById(epicTask1.getId()).getId());
        assertEquals("new EpicTask1", manager.getEpicTaskById(epicTask1.getId()).getName());
        assertEquals("Desription2", manager.getEpicTaskById(epicTask1.getId()).getDescription());
    }

    @Test
    public void shouldMoveTaskProgress() {
        manager.moveTaskToProgress(manager.getListTasks().getFirst().getId());
        assertEquals(Status.IN_PROGRESS, manager.getListTasks().getFirst().getStatus());
    }

    @Test
    public void shouldMoveTaskDone() {
        manager.moveTaskToDone(manager.getListTasks().getFirst().getId());
        assertEquals(Status.DONE, manager.getListTasks().getFirst().getStatus());
    }

    @Test
    public void shouldMoveSubTaskProgress() {
        manager.moveSubTaskToProgress(manager.getListSubTasks().getFirst().getId());
        assertEquals(Status.IN_PROGRESS, manager.getListSubTasks().getFirst().getStatus());
    }

    @Test
    public void shouldClearTasksEpic() {
        manager.clearListEpicTasks();
        assertEquals(0, manager.getListEpicTasks().size());
    }

    @Test
    public void shouldClearTasks() {
        manager.clearListTasks();
        assertEquals(0, manager.getListTasks().size());
    }

}