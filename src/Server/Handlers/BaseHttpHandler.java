package Server.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.Interfaces.TaskManager;
import com.yandex.app.service.Managers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class BaseHttpHandler implements HttpHandler {
    List<Task> tasks;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public BaseHttpHandler(List<Task> tasks){
        this.tasks = tasks;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);

        switch (endpoint){

            case Endpoint.GET_TASKS:
                handleGetTasks(exchange);
                break;
            case Endpoint.GET_SUBTASKS:
                handleGetTasks(exchange);
                break;
            case Endpoint.GET_EPICS:
                handleGetTasks(exchange);
                break;
        }

    }

    public void handleGetTasks(HttpExchange exchange) {

        String response = tasks.stream()
                .map(Task::toString)
                .collect(Collectors.joining("\n"));
        writeResponse(exchange,response, 200);
    }

    public void handleGetSubtasks(HttpExchange exchange){


    }

    public void writeResponse(HttpExchange exchange, String response, int code)  {
        try {
            try (OutputStream os = exchange.getResponseBody()) {
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(code, 0);
                os.write(response.getBytes(DEFAULT_CHARSET));
                os.flush();
                            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            exchange.close();
        }
    }

    public Endpoint getEndpoint(HttpExchange exchange){
        String path = exchange.getRequestURI().getPath();
        String[] pathArray = path.split("/");

        String method = exchange.getRequestMethod();

        switch (method){
            case "GET":
                if (pathArray.length == 2){
                    return switch (pathArray[1]) {
                        case "tasks" -> Endpoint.GET_TASKS;
                        case "subtasks" -> Endpoint.GET_SUBTASKS;
                        case "epics" -> Endpoint.GET_EPICS;
                        case "history" -> Endpoint.GET_HISTORY;
                        case "prioritized" -> Endpoint.GET_PRIORITIZED;
                        default -> Endpoint.UNKNOW;
                    };
                } else if (pathArray.length == 3) {
                    return switch (pathArray[1]) {
                        case "tasks" -> Endpoint.GET_TASKS_ID;
                        case "subtasks" -> Endpoint.GET_SUBTASKS_ID;
                        case "epics" -> Endpoint.GET_EPICS_ID;
                        default -> Endpoint.UNKNOW;
                    };
                } else if (pathArray.length == 4) {
                    if (pathArray[1].equals("epics") && pathArray[3].equals("subtasks")) {
                        return Endpoint.GET_EPICS_ID_SUBTASKS;
                    }
                    return Endpoint.UNKNOW;
                }
                return Endpoint.UNKNOW;
            case "POST":
                if (pathArray.length == 2){
                    return switch (pathArray[1]){
                        case "tasks" -> Endpoint.POST_TASKS;
                        case "subtasks" -> Endpoint.POST_SUBTASKS;
                        case "epics" -> Endpoint.POST_EPICS;
                        default -> Endpoint.UNKNOW;
                    };
                }
                return Endpoint.UNKNOW;
            case "DELETE":
                if (pathArray.length == 3){
                    return switch (pathArray[1]){
                        case "tasks" -> Endpoint.DELETE_TASKS_ID;
                        case "subtasks" -> Endpoint.DELETE_SUBTASKS_ID;
                        case "epics" -> Endpoint.DELETE_EPICS_ID;
                        default -> Endpoint.UNKNOW;
                    };
                }
                return Endpoint.UNKNOW;
            default:
                return Endpoint.UNKNOW;
        }
    }

    enum Endpoint {
        GET_TASKS, GET_TASKS_ID, POST_TASKS, DELETE_TASKS_ID,
        GET_SUBTASKS, GET_SUBTASKS_ID, POST_SUBTASKS, DELETE_SUBTASKS_ID,
        GET_EPICS, GET_EPICS_ID, GET_EPICS_ID_SUBTASKS, POST_EPICS, DELETE_EPICS_ID,
        GET_HISTORY, GET_PRIORITIZED, UNKNOW
    }
}
