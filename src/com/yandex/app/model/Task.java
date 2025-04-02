package com.yandex.app.model;
import com.yandex.app.service.TaskManager;
import java.util.Objects;

public class Task {
    protected String task;
    protected String description;
    protected int id;
    protected Status status;

    public Task(String task, String description){
        this.task = task;
        this.description = description;
        this.status = Status.NEW;
    }

    public String getTask(){
        return task;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setStatus(Status status){
        this.status = status;
    }
    public Status getStatus(){
        return status;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Task task1 = (Task) object;    //стоит ли добавлять статус в переопределение equals?
        return id == task1.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
