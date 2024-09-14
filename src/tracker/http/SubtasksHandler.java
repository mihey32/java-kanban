package tracker.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import tracker.controllers.TaskManager;
import tracker.exception.ManagerSaveException;
import tracker.http.adapter.LocalDateTimeAdapter;
import tracker.model.Subtask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


public class SubtasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public SubtasksHandler(TaskManager taskManager) {
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

        if ("GET".equals(method) && "/subtasks".equals(path)) {
            handleGetSubtasks(exchange, taskManager);
        } else if ("GET".equals(method) && path.startsWith("/subtasks/")) {
            handleGetSubtaskById(exchange, taskManager);
        } else if ("POST".equals(method) && "/subtasks".equals(path)) {
            handlePostSubtask(exchange, taskManager);
        } else if ("DELETE".equals(method) && path.startsWith("/subtasks/")) {
            handleDeleteSubtask(exchange, taskManager);
        } else {
            System.out.println("Неизвестный тип запроса");
            sendNotFound(exchange);
        }
    }

    private void handleGetSubtasks(HttpExchange exchange, TaskManager taskManager) throws IOException {
        System.out.println("Обработка GET-запроса для всех подзадач");
        String response = gson.toJson(taskManager.getSubtasks());
        if (response != null && !response.isEmpty()) {
            System.out.println("Ответ сервера: " + response);
            sendText(exchange, response);
        } else {
            System.out.println("Ответ null или пустой");
            sendNotFound(exchange);
        }
    }

    private void handleGetSubtaskById(HttpExchange exchange, TaskManager taskManager) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        if (pathParts.length != 3) {
            sendNotFound(exchange);
            return;
        }

        try {
            int subTaskId = Integer.parseInt(pathParts[2]);
            Subtask subTask = taskManager.getSubtaskById(subTaskId);
            if (subTask == null) {
                sendNotFound(exchange);
            } else {
                String response = gson.toJson(subTask);
                sendText(exchange, response);
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handlePostSubtask(HttpExchange exchange, TaskManager taskManager) throws IOException {

        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String requestBody = sb.toString();
        Subtask subTask;

        try {
            subTask = gson.fromJson(requestBody, Subtask.class);
        } catch (JsonSyntaxException e) {
            System.out.println("Неверный формат");
            sendNotFound(exchange);
            return;
        }

        if (subTask == null) {
            sendNotFound(exchange);
            return;
        }

        Integer subTaskId = subTask.getId();
        if (subTaskId != null) {
            try {
                System.out.println("Обновить подзадачу: " + subTaskId);
                taskManager.updateSubTask(subTask);
                System.out.println("Подазача успешно обновлена");
            } catch (ManagerSaveException e) {
                System.out.println(e.getMessage());
                sendHasInteractions(exchange);
            }
        } else {
            taskManager.createSubTask(subTask);
            System.out.println("Создана новая подзадача с ID " + subTask.getId());
        }

        String response = gson.toJson(subTask);
        sendText(exchange, response);
    }

    private void handleDeleteSubtask(HttpExchange exchange, TaskManager taskManager) throws IOException {
        // Логика для обработки запроса на удаление подтзадачи по ID с использованием TaskManager
        System.out.println("delete");
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        if (pathParts.length != 3) {
            sendNotFound(exchange);
            return;
        }

        try {
            int subTaskId = Integer.parseInt(pathParts[2]);
            Subtask subTask = taskManager.getSubtaskById(subTaskId);
            if (subTask == null) {
                sendNotFound(exchange);
            } else {
                taskManager.deleteTaskById(subTask.getId());
                sendText(exchange, "Подзадача c ID: " + subTask.getId() + " удалена");
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }
}