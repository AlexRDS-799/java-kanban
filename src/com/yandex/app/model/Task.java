package com.yandex.app.model;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected int epicId;
    protected Status status;
    protected TaskType taskType = TaskType.TASK;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public String getName() {
        return name;
    }

    public String getDescription(){
        return description;
    }

    public int getEpicId() {
        return epicId;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public TaskType getTaskType(){
        return taskType;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Task task1 = (Task) object;
        return id == task1.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.description;
    }
}
