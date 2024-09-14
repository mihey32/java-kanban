package tracker.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import tracker.controllers.TaskManager;
import tracker.exception.ManagerSaveException;
import tracker.http.adapter.LocalDateTimeAdapter;
import tracker.model.Task;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class TasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        handle(exchange, this.taskManager);
    }

    @Override
    public void handle(HttpExchange exchange, TaskManager taskManager) throws IOException {

        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        System.out.println("Получен запрос: " + method + " " + path);

        if ("GET".equals(method) && "/tasks".equals(path)) {
            handleGetTasks(exchange, taskManager);
        } else if ("GET".equals(method) && path.startsWith("/tasks/")) {
            handleGetTaskById(exchange, taskManager);
        } else if ("POST".equals(method) && "/tasks".equals(path)) {
            handlePostTask(exchange, taskManager);
        } else if ("DELETE".equals(method) && path.startsWith("/tasks/")) {
            handleDeleteTask(exchange, taskManager);
        } else {
            System.out.println("Неизвестный тип запроса");
            sendNotFound(exchange);
        }
    }

    private void handleGetTasks(HttpExchange exchange, TaskManager taskManager) throws IOException {
        System.out.println("Обработка GET-запроса для всех задач");
        String response = gson.toJson(taskManager.getTasks());
        if (response != null && !response.isEmpty()) {
            System.out.println("Ответ сервера: " + response);
            sendText(exchange, response);
        } else {
            System.out.println("Ответ null или пустой");
            sendNotFound(exchange);
        }
    }

    private void handleGetTaskById(HttpExchange exchange, TaskManager taskManager) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        if (pathParts.length != 3) {
            sendNotFound(exchange);
            return;
        }

        try {
            int taskId = Integer.parseInt(pathParts[2]);
            Task task = taskManager.getTaskById(taskId);
            if (task == null) {
                sendNotFound(exchange);
            } else {
                String response = gson.toJson(task);
                sendText(exchange, response);
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handlePostTask(HttpExchange exchange, TaskManager taskManager) throws IOException {

        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String requestBody = sb.toString();
        Task task;

        try {
            task = gson.fromJson(requestBody, Task.class);
        } catch (JsonSyntaxException e) {
            System.out.println("Неверный формат");
            sendNotFound(exchange);
            return;
        }

        if (task == null) {
            sendNotFound(exchange);
            return;
        }

        Integer taskId = task.getId();
        if (taskId != null) {
            try {
                System.out.println("Обновить задачу: " + taskId);
                taskManager.updateTask(task);
                System.out.println("Задача успешно обновлена");
            } catch (ManagerSaveException e) {
                System.out.println(e.getMessage());
                sendHasInteractions(exchange);
            }
        } else {
            taskManager.createTask(task);
            System.out.println("Создана новая задача с ID " + task.getId());
        }

        String response = gson.toJson(task);
        sendText(exchange, response);
    }

    private void handleDeleteTask(HttpExchange exchange, TaskManager taskManager) throws IOException {

        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        if (pathParts.length != 3) {
            sendNotFound(exchange);
            return;
        }

        try {
            int taskId = Integer.parseInt(pathParts[2]);
            Task task = taskManager.getTaskById(taskId);
            if (task == null) {
                sendNotFound(exchange);
            } else {
                taskManager.deleteTaskById(task.getId());
                sendText(exchange, "Задача c ID: " + task.getId() + " удалена");
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }
}