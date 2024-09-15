package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.controllers.InMemoryTaskManager;
import tracker.controllers.TaskManager;
import tracker.enums.Status;
import tracker.http.HttpTaskServer;
import tracker.http.adapter.LocalDateTimeAdapter;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    static HttpTaskServer httpTaskServer;
    static Gson gson;
    static TaskManager taskManager;
    static final LocalDateTime TASK_DATE = LocalDateTime.of(2024, 1, 1, 0, 0);
    static final LocalDateTime SUB_DATE = LocalDateTime.of(2024, 3, 10, 0, 0);

    private Task task;
    private Epic epic;
    private Subtask subTask;


    @BeforeEach
    void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        httpTaskServer = new HttpTaskServer(taskManager);

        httpTaskServer.start();

        taskManager.deleteAllTasks();
        taskManager.deleteAllSubtasks();
        taskManager.deleteAllEic();

        task = new Task("Задача 1", "Описание задачи 1", TASK_DATE, 1000);
        epic = new Epic("Эпик 1", "Описание эпика 1");

        taskManager.createTask(task);
        taskManager.createEpic(epic);

        subTask = new Subtask("Подзадача 1", "Описание подзадачи 1", epic.getId(), SUB_DATE, 1000);
        taskManager.createSubTask(subTask);

        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);

        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @AfterEach
    void shutDown() {
        httpTaskServer.stop();
    }

    @Test
    void getAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<List<Task>>() {
        }.getType();

        List<Task> tasksList = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "Код ответа не 200");
        assertNotNull(tasksList, "Список задач не получен");
        assertEquals(taskManager.getTasks(), tasksList, "Получен неверный список задач");
    }

    @Test
    void getAllEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type epicType = new TypeToken<List<Epic>>() {
        }.getType();

        List<Epic> epicList = gson.fromJson(response.body(), epicType);

        assertEquals(200, response.statusCode(), "Код ответа не 200");
        assertNotNull(epicList, "Список задач не получен");
        assertEquals(taskManager.getEpics(), epicList, "Получен неверный список задач");
    }

    @Test
    void getAllSubTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type subTaskType = new TypeToken<List<Subtask>>() {
        }.getType();

        List<Subtask> subTaskList = gson.fromJson(response.body(), subTaskType);

        assertEquals(200, response.statusCode(), "Код ответа не 200");
        assertNotNull(subTaskList, "Список подзадач не получен");
        assertEquals(taskManager.getSubtasks(), subTaskList, "Получен неверный список подзадач");
    }

    @Test
    void getTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskDeserialized = gson.fromJson(response.body(), Task.class);

        assertEquals(200, response.statusCode(), "Код ответа не 200");
        assertNotNull(taskDeserialized, "Задача не получена");
        assertEquals(taskManager.getTaskById(1), taskDeserialized, "Получена неверная задача");
    }

    @Test
    void getEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epicDeserialized = gson.fromJson(response.body(), Epic.class);

        assertEquals(200, response.statusCode(), "Код ответа не 200");
        assertNotNull(epicDeserialized, "Эпик не получена");
        assertEquals(taskManager.getEpicById(2), epicDeserialized, "Получен неверный эпик");
    }

    @Test
    void getSubTasksById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subTaskDeserialized = gson.fromJson(response.body(), Subtask.class);

        assertEquals(200, response.statusCode(), "Код ответа не 200");
        assertNotNull(subTaskDeserialized, "Подзадача не получена");
        assertEquals(taskManager.getSubtaskById(3), subTaskDeserialized, "Получена неверная подзадача");
    }

    @Test
    void getTaskIncorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080//tasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Код ответа не 404");
    }

    @Test
    void getEpicIncorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Код ответа не 404");
    }

    @Test
    void getSubtasksIncorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/a");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Код ответа не 404");
    }

    @Test
    void getSubtasksByOneEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/2/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type subtaskType = new TypeToken<List<Subtask>>() {
        }.getType();
        List<Subtask> subtasksList = gson.fromJson(response.body(), subtaskType);

        assertEquals(200, response.statusCode(), "Код ответа не 200");
        assertNotNull(subtasksList, "Список подзадач не получен");
        assertEquals(taskManager.getSubtasksOfEpic(epic), subtasksList, "Получен неверный список подзадач");
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> history = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "Код ответа не 200");
        assertNotNull(history, "Список истории не получен");
        assertEquals(3, history.size(), "Длина списка истории не 3");
        assertEquals(taskManager.getHistory().get(0).getId(), history.get(0).getId(),
                "Id первого элемента списка не совпадает");
        assertEquals(taskManager.getHistory().get(1).getId(), history.get(1).getId(),
                "Id второго элемента списка не совпадает");
        assertEquals(taskManager.getHistory().get(2).getId(), history.get(2).getId(),
                "Id третьего элемента списка не совпадает");
    }

    @Test
    void getPrioritizedTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> priority = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "Код ответа не 200");
        assertNotNull(priority, "Список приоритетных задач не получен");
        assertEquals(2, priority.size(), "Длина списка приоритетных задач не 3");
        assertEquals(taskManager.getPrioritizedTasks().get(0).getId(), priority.get(0).getId(),
                "Id первого элемента списка не совпадает");
        assertEquals(taskManager.getPrioritizedTasks().get(1).getId(), priority.get(1).getId(),
                "Id третьего элемента списка не совпадает");
    }


    @Test
    void addNewTask() throws IOException, InterruptedException {
        Task task2 = new Task("Задача 2", "Описание 2",
                LocalDateTime.of(2024, 6, 7, 10, 0), 1000);
        String json = gson.toJson(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не 201");
        assertEquals(4, taskManager.getTasks().get(1).getId(), "Id новой задачи не совпадает");
        assertEquals(2, taskManager.getTasks().size(), "Новая задача не добавлена");
    }

    @Test
    void addNewEpic() throws IOException, InterruptedException {
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        String json = gson.toJson(epic2);


        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не 201");
        assertEquals(4, taskManager.getEpics().get(1).getId(), "Id нового эпика не совпадает");
        assertEquals(2, taskManager.getEpics().size(), "Новый эпик не добавлен");
    }

    @Test
    void addNewSubtasks() throws IOException, InterruptedException {
        Subtask subTask2 = new Subtask("Подзадача 3", "Описание подзадачи 3", epic.getId(),
                LocalDateTime.of(2024, 2, 10, 13, 0), 1000);
        String json = gson.toJson(subTask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не 201");
        assertEquals(4, taskManager.getSubtasks().get(1).getId(), "Id нового эпика не совпадает");
        assertEquals(2, taskManager.getSubtasks().size(), "Новый эпик не добавлен");
    }

    @Test
    void updateNewTask() throws IOException, InterruptedException {
        Task taskUpdate = new Task(task.getId(), "Задача 1 обновление", "Описание задачи 1 обновление",
                Status.NEW, TASK_DATE, 1000);
        String json = gson.toJson(taskUpdate);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не 201");
        assertEquals(1, taskManager.getTasks().getFirst().getId(), "Id обновленной задачи не совпадает");
    }

    //
    @Test
    void updateNewEpic() throws IOException, InterruptedException {
        Epic epic2 = new Epic(epic.getId(), "Эпик 1 обновление", "Описание эпика 1 обновление",
                epic.getStatus(), epic.getStartTime(), epic.getDuration(), epic.getEndTime());
        String json = gson.toJson(epic2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");


        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не 201");
        assertEquals(2, taskManager.getEpics().getFirst().getId(), "Id нового эпика не совпадает");
    }

    @Test
    void updateNewSubtasks() throws IOException, InterruptedException {
        LocalDateTime timeUpdate = LocalDateTime.of(2024, 9, 14, 12, 0);
        Subtask subTask2 = new Subtask(subTask.getId(), "Подзадача 3", "Описание подзадачи 3",
                Status.IN_PROGRESS, timeUpdate, 60, epic.getId());
        String jsonSubtask = gson.toJson(subTask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonEpic = gson.toJson(epic);
        URI url2 = URI.create("http://localhost:8080/epics");
        HttpRequest.BodyPublisher bodyEpic = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(url2).POST(bodyEpic).build();
        HttpResponse<String> responseEpic = client.send(requestEpic, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не 201");
        assertEquals(200, responseEpic.statusCode(), "Код ответа не 201");

        assertEquals(3, taskManager.getSubtasks().getFirst().getId(), "Id обновленной подзадачи не совпадает");
        assertEquals(Status.IN_PROGRESS, taskManager.getSubtasks().getFirst().getStatus(), "Cтатус подзадачи не изменился");
        assertEquals(Status.IN_PROGRESS, taskManager.getEpics().getFirst().getStatus(), "Cтатус эпика не изменился");

    }

    @Test
    void removeTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не 200");
        Assertions.assertTrue(taskManager.getTasks().isEmpty(), "Задача не удалена");
        assertNull(taskManager.getTaskById(0), "Задача не удалена");
    }

    @Test
    void removeEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа не 200");
        Assertions.assertTrue(taskManager.getEpics().isEmpty(), "Эпик не удален");
        assertNull(taskManager.getEpicById(1), "Эпик не удален");
    }

    @Test
    void removeSubtaskById() {
        HttpClient client = HttpClient.newHttpClient();
        System.out.println(taskManager.getSubtasks());
        URI url = URI.create("http://localhost:8080/subtasks/3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Код ответа не 200");
            Assertions.assertTrue(taskManager.getSubtasks().isEmpty(), "Подзадача не удалена");
            assertNull(taskManager.getSubtaskById(2), "Подзадача не удалена");
        } catch (IOException e) {
            System.err.println("IOException occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("InterruptedException occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

}