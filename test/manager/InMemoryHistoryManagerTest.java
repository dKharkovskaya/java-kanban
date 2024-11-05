package manager;

import org.junit.jupiter.api.Test;
import task.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    Task task = new Task(1, "Task1", "Descr1");

    @Test
    void shouldAdd1TaskInHistory() {
        inMemoryHistoryManager.add(task);
        assertEquals(1, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    void shouldAdd2TaskInHistory() {
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(task);
        assertEquals(1, inMemoryHistoryManager.getHistory().size());
    }
}