package Server.Handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler{

    public void sendText(HttpExchange exchange, String response){
        byte[] resp = response.getBytes(StandardCharsets.UTF_8);
        try {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, resp.length);
            exchange.getResponseBody().write(resp);
            exchange.close();
        }catch (IOException e){
            throw new RuntimeException();
        }
    }

    public void sendNotFound(HttpExchange exchange, String text, int id){
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        try {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(id, resp.length);
            exchange.getResponseBody().write(resp);
            exchange.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendHasInteractions(HttpExchange exchange, String text){

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
