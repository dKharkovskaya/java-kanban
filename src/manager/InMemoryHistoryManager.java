package manager;

import task.Task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Map <Integer, Node<Task>> historyTasks = new LinkedHashMap<>();
    private Node<Task> tail;

    @Override
    public void add(Task task) {
        if (historyTasks.containsKey((int)task.getId())){
            remove((int)task.getId());
        }
        linkLast(task);
    }
    @Override
    public void remove(int id) {
        removeNode(historyTasks.get(id));
    }
    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(null, task, oldTail);
        tail = newNode;
        historyTasks.put((int)task.getId(), tail);
        if (oldTail == null)
            tail = newNode;
        else
            oldTail.prev= newNode;
    }
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        for (Map.Entry<Integer, Node<Task>> listTask : historyTasks.entrySet()){
            tasks.add(listTask.getValue().data);
        }
        return  tasks;
    }

    private void removeNode(Node task) {
        if (task.next == null){
            task.prev.next = null;
        } else if (task.prev == null) {
            task.next.prev = null;
        } else {
            Node next = task.next;
            Node prev = task.prev;
            prev.next = next;
            next.prev = prev;
        }
        task.prev = null;
        task.next = null;
    }
    static class Node<E extends Task> {
        public E data;
        public Node<E> next;
        public Node<E> prev;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

}
