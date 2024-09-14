package tracker.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import tracker.controllers.TaskManager;
import tracker.http.adapter.LocalDateTimeAdapter;
import tracker.model.Epic;
import tracker.model.Subtask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public EpicsHandler(TaskManager taskManager) {
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

        if ("GET".equals(method) && "/epics".equals(path)) {
            handleGetEpics(exchange, taskManager);
        } else if ("GET".equals(method) && path.startsWith("/epics/") && !path.endsWith("/subtasks")) {
            handleGetEpicById(exchange, taskManager);
        } else if ("GET".equals(method) && path.endsWith("/subtasks")) {
            handleGetSubtaskByEpicId(exchange, taskManager);
        } else if ("POST".equals(method) && "/epics".equals(path)) {
            handlePostEpic(exchange, taskManager);
        } else if ("DELETE".equals(method) && path.startsWith("/epics/")) {
            handleDeleteEpic(exchange, taskManager);
        } else {
            System.out.println("Неизвестный тип запроса");
            sendNotFound(exchange);
        }
    }

    private void handleGetEpics(HttpExchange exchange, TaskManager taskManager) throws IOException {
        System.out.println("Обработка GET-запроса для всех эпиков");
        String response = gson.toJson(taskManager.getEpics());
        if (response != null && !response.isEmpty()) {
            System.out.println("Ответ сервера: " + response);
            sendText(exchange, response);
        } else {
            System.out.println("Ответ null или пустой");
            sendNotFound(exchange);
        }
    }

    private void handleGetEpicById(HttpExchange exchange, TaskManager taskManager) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        if (pathParts.length != 3) {
            sendNotFound(exchange);
            return;
        }

        try {
            int epicId = Integer.parseInt(pathParts[2]);
            Epic epic = taskManager.getEpicById(epicId);
            if (epic == null) {
                sendNotFound(exchange);
            } else {
                String response = gson.toJson(epic);
                sendText(exchange, response);
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handleGetSubtaskByEpicId(HttpExchange exchange, TaskManager taskManager) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        if (pathParts.length != 4) {
            sendNotFound(exchange);
            return;
        }

        try {
            int epicId = Integer.parseInt(pathParts[2]);

            List<Subtask> subTasks = taskManager.getSubtasksOfEpic(taskManager.getEpicById(epicId));
            if (subTasks == null) {
                sendNotFound(exchange);
            } else {
                String response = gson.toJson(subTasks);
                sendText(exchange, response);
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handlePostEpic(HttpExchange exchange, TaskManager taskManager) throws IOException {

        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String requestBody = sb.toString();
        Epic epic;

        try {
            epic = gson.fromJson(requestBody, Epic.class);
        } catch (JsonSyntaxException e) {
            System.out.println("Неверный формат");
            sendNotFound(exchange);
            return;
        }

        if (epic == null) {
            sendNotFound(exchange);
            return;
        }

        Integer epicId = epic.getId();
        if (epicId != null) {
            taskManager.updateEpic(epic);
        } else {
            taskManager.createEpic(epic);
        }

        String response = gson.toJson(epic);
        sendText(exchange, response);
    }

    private void handleDeleteEpic(HttpExchange exchange, TaskManager taskManager) throws IOException {

        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        if (pathParts.length != 3) {
            sendNotFound(exchange);
            return;
        }

        try {
            int epicId = Integer.parseInt(pathParts[2]);
            Epic epic = taskManager.getEpicById(epicId);
            if (epic == null) {
                sendNotFound(exchange);
            } else {
                taskManager.deleteEpicById(epic.getId());
                sendText(exchange, "Эпик c ID: " + epic.getId() + " удален");
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }
}