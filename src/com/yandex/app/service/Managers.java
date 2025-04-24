package com.yandex.app.service;

public class Managers {

    public TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

}
