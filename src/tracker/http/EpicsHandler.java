package tracker.http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import tracker.controllers.Managers;
import tracker.controllers.TaskManager;
import tracker.model.Epic;
import tracker.model.Subtask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicsHandler(TaskManager taskManager) {
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
            sendNotFound(exchange);
        }
    }

    private void handleGetEpics(HttpExchange exchange, TaskManager taskManager) throws IOException {
        String response = gson.toJson(taskManager.getEpics());
        if (response != null && !response.isEmpty()) {
            sendText(exchange, response);
        } else {
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