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

    public HttpResponse<String> getTasks() throws IOException, InterruptedException {
        HttpRequest requestGetTasks = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriStandart + "/tasks"))
                .build();

        return client.send(requestGetTasks, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getTasksId(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriStandart + "/tasks/" + id))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> addTask(Task task) {
        Gson gson = getGson();
        String json = gson.toJson(task);
        HttpResponse<String> response;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriStandart + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                System.out.println("Успешно добавлена задача: " + task.getName());
            }
            if (response.statusCode() >= 400 && response.statusCode() <= 499) {
                System.out.println("Проблема с запросом на добавление задачи: " + response.statusCode());
                System.out.println(response.body());
            }
        } catch (IOException | InterruptedException e) {
            String errorMessage = "Ошибка при отправке запроса: " + e.getMessage();
            throw new RuntimeException(errorMessage, e);
        }
        return response;
    }

    public HttpResponse<String> deleteTask(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriStandart + "/tasks/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    //САБТАСКИ

    public HttpResponse<String> getSubtasks() throws IOException, InterruptedException {
        HttpRequest requestSubtasks = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriStandart + "/subtasks"))
                .build();

        return client.send(requestSubtasks, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getSubtasksId(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriStandart + "/subtasks/" + id))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> addSubtask(Subtask subtask) throws IOException, InterruptedException {
        Gson gson = getGson();
        String json = gson.toJson(subtask);
        HttpResponse<String> response;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriStandart + "/subtasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                System.out.println("Успешно добавлен сабтаск: " + subtask.getName());
            }
            if (response.statusCode() >= 400 && response.statusCode() <= 499) {
                System.out.println("Проблема с запросом на добавление сабтаска: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            String errorMessage = "Ошибка при отправке запроса: " + e.getMessage();
            throw new RuntimeException(errorMessage, e);
        }
        return response;

    }

    public HttpResponse<String> deleteSubtasksId(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriStandart + "/subtasks/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    //ЭПИКИ

    public HttpResponse<String> getEpic() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriStandart + "/epics"))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getEpicId(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriStandart + "/epics/" + id))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getEpicsIdSubtasks(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriStandart + "/epics/" + id + "/subtasks"))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> addEpic(Epic epic) throws IOException, InterruptedException {
        Gson gson = getGson();

        String json = gson.toJson(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriStandart + "/epics"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> deleteEpicsId(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriStandart + "/epics/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    //ИСТОРИЯ

    public HttpResponse<String> getHistory() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriStandart + "/history"))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    //Задачи по Приоритету

    public HttpResponse<String> getPrioritized() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriStandart + "/prioritized"))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        return gsonBuilder.create();
    }

}
