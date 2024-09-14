package tracker.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import tracker.controllers.TaskManager;
import tracker.http.adapter.LocalDateTimeAdapter;
import java.io.IOException;
import java.time.LocalDateTime;

public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public HistoryHandler(TaskManager taskManager) {
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

        if ("GET".equals(method) && "/history".equals(path)) {
            handleGetHistory(exchange, taskManager);
        } else {
            System.out.println("Неизвестный тип запроса");
            sendNotFound(exchange);
        }
    }

    private void handleGetHistory(HttpExchange exchange, TaskManager taskManager) throws IOException {
        String response = gson.toJson(taskManager.getHistory());
        if (response != null && !response.isEmpty()) {
            System.out.println("Ответ сервера: " + response);
            sendText(exchange, response);
        } else {
            System.out.println("Ответ null или пустой");
            sendNotFound(exchange);
        }
    }
}