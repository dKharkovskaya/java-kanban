package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();
    private static final int HISTORY_SIZE = 10;

    @Override
    public void add(Task task) {
        if (history.size() < HISTORY_SIZE) {
            history.add(task);
        } else {
            history.removeFirst();
        }
    }
    @Override
    public List<Task> getHistory() {
        return history;
    }
}
