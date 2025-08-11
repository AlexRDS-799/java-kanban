package Server.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.app.model.Epic;
import com.yandex.app.service.Interfaces.TaskManager;

import java.io.IOException;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager fileBackedTaskManager;

    public EpicsHandler(TaskManager fileBackedTaskManager){
        this.fileBackedTaskManager = fileBackedTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }

    public void handleGetEpics(){

    }

    public void handleGetEpicsId(){

    }

    public void handleGetEpicsIdSubtasks(){

    }

    public void handlePostEpics(){

    }

    public void handleDeleteEpicsId(){

    }
}
