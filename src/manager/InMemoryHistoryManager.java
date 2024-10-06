package manager;

import task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private int cntHistoryTask = 0;
    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (cntHistoryTask < 9) {
            history.add(cntHistoryTask, task);
            cntHistoryTask += 1;
        } else if (cntHistoryTask == 9) {
            history.add(cntHistoryTask, task);
            cntHistoryTask = 0;
        }
    }
    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}
