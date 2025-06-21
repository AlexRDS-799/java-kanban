package com.yandex.app.service.In_Memory;

import com.yandex.app.model.Task;
import com.yandex.app.service.Interfaces.HistoryManager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    private Map<Integer, Node<Integer, Task>> history = new HashMap<>();
    private Node<Integer, Task> head = null;
    private Node<Integer, Task> tail = null;


    @Override
    public void add(Task task) {
        if (history.containsKey(task.getId())) {
            remove(task);
        }
        linkLast(task);
    }

    @Override
    public void remove(Task task) {
        Node<Integer, Task> node = history.remove(task.getId());
        removeNode(node);

    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> arrayHistory = new ArrayList<>();
        Node current = head;
        while (current != null) {
            arrayHistory.add((Task) current.task);
            current = current.next;
        }
        return arrayHistory;
    }

    public void linkLast(Task task) {
        Node<Integer, Task> newNode = new Node<>(task.getId(), task);

        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prew = tail;
            tail = newNode;
        }
        history.put(newNode.key, newNode);
    }

    public void removeNode(Node<Integer, Task> node) {

        if (node == null) {
            return;
        }

        if (node.prew != null) {
            node.prew.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prew = node.prew;
        } else {
            tail = node.prew;
        }
    }
}
