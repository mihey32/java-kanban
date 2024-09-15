package tracker.http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import tracker.controllers.Managers;
import tracker.controllers.TaskManager;
import tracker.exception.ManagerSaveException;
import tracker.model.Task;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class TasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = Managers.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        handle(exchange, this.taskManager);
    }

    @Override
    public void handle(HttpExchange exchange, TaskManager taskManager) throws IOException {

        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if ("GET".equals(method) && "/tasks".equals(path)) {
            handleGetTasks(exchange, taskManager);
        } else if ("GET".equals(method) && path.startsWith("/tasks/")) {
            handleGetTaskById(exchange, taskManager);
        } else if ("POST".equals(method) && "/tasks".equals(path)) {
            handlePostTask(exchange, taskManager);
        } else if ("DELETE".equals(method) && path.startsWith("/tasks/")) {
            handleDeleteTask(exchange, taskManager);
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleGetTasks(HttpExchange exchange, TaskManager taskManager) throws IOException {
        String response = gson.toJson(taskManager.getTasks());
        if (response != null && !response.isEmpty()) {
            sendText(exchange, response);
        } else {
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
                taskManager.updateTask(task);
            } catch (ManagerSaveException e) {
                sendHasInteractions(exchange);
            }
        } else {
            taskManager.createTask(task);
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