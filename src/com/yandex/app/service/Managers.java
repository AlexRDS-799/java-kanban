package com.yandex.app.service;

import com.yandex.app.service.In_Memory.InMemoryHistoryManager;
import com.yandex.app.service.In_Memory.InMemoryTaskManager;
import com.yandex.app.service.Interfaces.HistoryManager;
import com.yandex.app.service.Interfaces.TaskManager;

public class Managers {

    public static TaskManager getDefault(){
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.resetIdTask(); //в объекте InMemoryTaskManager переменна idTask является static. При создании
                                            //объекта класса переменная не обнуляется и счетчик задач не скидывается. Без
                                            // данного метода тесты не проходят.
        return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

}
