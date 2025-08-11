package Server;

import Server.Handlers.DurationTypeAdapter;
import Server.Handlers.LocalDateTimeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

}
