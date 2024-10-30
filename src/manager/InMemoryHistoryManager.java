package manager;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    public Map<Integer, Node<Task>> historyTasks = new HashMap<>();
    private int size = 0;
    private Node<Task> tail;
    private Node<Task> head;

    @Override
    public void add(Task task) {
        if (historyTasks.containsKey((int) task.getId())) {
            remove((int) task.getId());
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

    private void linkLast(Task task) {
        Node<Task> newNode;
        if (size == 0) {
            newNode = new Node<>(null, task, null);
            head = newNode;
            tail = newNode;
        } else {
            newNode = new Node<>(tail, task, null);
            tail.next = newNode;
            tail = newNode;
        }
        size++;
        historyTasks.put((int) task.getId(), newNode);
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        for (Map.Entry<Integer, Node<Task>> listTask : historyTasks.entrySet()) {
            tasks.add(listTask.getValue().data);
        }
        return tasks;
    }

    private void removeNode(Node task) {
        if (size == 0) {
            return;
        }
        if (size == 1) {
            head = null;
            tail = null;
            task.data = null;
            size = 0;
            return;
        } else if (task == tail) {
            task.prev.next = null;
            task.prev = null;
            task.data = null;
        } else if (task == head) {
            task.next.prev = null;
            task.next = null;
            task.data = null;
        } else {
            task.prev = task.next.next;
            task.next = task.prev.prev;
            task.data = null;
        }
        size --;
    }

    private static class Node<E extends Task> {
        E data;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

}