package server;

import server.handlers.DurationTypeAdapter;
import server.handlers.LocalDateTimeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskTrackerAPI {
    HttpClient client = HttpClient.newHttpClient();
    URI uriStandart = URI.create("http://localhost:8080");

    //ТАСКИ

    public void getTasks() throws IOException, InterruptedException {
        HttpRequest requestGetTasks = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriStandart + "/tasks"))
                .build();

        client.send(requestGetTasks, HttpResponse.BodyHandlers.ofString());
    }

    public void getTasksId(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriStandart + "/tasks/" + id))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> addTask(Task task) throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        Gson gson = gsonBuilder.create();

        String json = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriStandart + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void deleteTask(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriStandart + "/tasks/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    //САБТАСКИ

    public void getSubtasks() throws IOException, InterruptedException {
        HttpRequest requestSubtasks = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriStandart + "/subtasks"))
                .build();

        client.send(requestSubtasks, HttpResponse.BodyHandlers.ofString());
    }

    public void getSubtasksId(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriStandart + "/subtasks/" + id))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> addSubtask(Subtask subtask) throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        Gson gson = gsonBuilder.create();

        String json = gson.toJson(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriStandart + "/subtasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());

    }

    public void deleteSubtasksId(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriStandart + "/subtasks/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    //ЭПИКИ

    public void getEpic() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriStandart + "/epics"))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void getEpicId(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriStandart + "/epics/" + id))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void getEpicsIdSubtasks(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriStandart + "/epics/" + id + "/subtasks"))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> addEpic(Epic epic) throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        Gson gson = gsonBuilder.create();

        String json = gson.toJson(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriStandart + "/epics"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void deleteEpicsId(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriStandart + "/epics/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    //ИСТОРИЯ

    public void getHistory() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriStandart + "/history"))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    //Задачи по Приоритету

    public void getPrioritized() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriStandart + "/prioritized"))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
