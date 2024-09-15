package tracker.http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import tracker.controllers.Managers;
import tracker.controllers.TaskManager;
import tracker.exception.ManagerSaveException;
import tracker.model.Subtask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class SubtasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtasksHandler(TaskManager taskManager) {
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

        if ("GET".equals(method) && "/subtasks".equals(path)) {
            handleGetSubtasks(exchange, taskManager);
        } else if ("GET".equals(method) && path.startsWith("/subtasks/")) {
            handleGetSubtaskById(exchange, taskManager);
        } else if ("POST".equals(method) && "/subtasks".equals(path)) {
            handlePostSubtask(exchange, taskManager);
        } else if ("DELETE".equals(method) && path.startsWith("/subtasks/")) {
            handleDeleteSubtask(exchange, taskManager);
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleGetSubtasks(HttpExchange exchange, TaskManager taskManager) throws IOException {
        String response = gson.toJson(taskManager.getSubtasks());
        if (response != null && !response.isEmpty()) {
            sendText(exchange, response);
        } else {
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
                taskManager.updateSubTask(subTask);
            } catch (ManagerSaveException e) {
                sendHasInteractions(exchange);
            }
        } else {
            taskManager.createSubTask(subTask);
        }

        String response = gson.toJson(subTask);
        sendText(exchange, response);
    }

    private void handleDeleteSubtask(HttpExchange exchange, TaskManager taskManager) throws IOException {
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