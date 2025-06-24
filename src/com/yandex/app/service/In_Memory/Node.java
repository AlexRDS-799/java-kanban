package com.yandex.app.service.In_Memory;

public class Node<K, T> {
    K key;
    T task;
    Node<K, T> prew;
    Node<K, T> next;

    public Node(K key, T task) {
        this.key = key;
        this.task = task;
        this.prew = null;
        this.next = null;
    }

    @Override
    public String toString() {
        return task.toString();
    }
}
