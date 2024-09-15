package tracker;

import tracker.controllers.Managers;
import tracker.controllers.TaskManager;
import tracker.http.HttpTaskServer;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

public class Main {
    public static void main(String[] args) throws IOException {

        TaskManager taskManager = Managers.getTaskManager();
        LocalDateTime time1 = LocalDateTime.of(2020, Month.JANUARY, 4, 1, 0, 0);
        LocalDateTime time2 = LocalDateTime.of(2020, Month.JANUARY, 18, 0, 0, 0);
        LocalDateTime time3 = LocalDateTime.of(2021, Month.JANUARY, 8, 1, 0, 0);

        Task task = new Task("Задача 1", "Описание задачи 1", time1, 60);
        Task task2 = new Task("Задача 1", "Описание задачи 1", time2, 60);
        taskManager.createTask(task);
        taskManager.createTask(task2);
        Epic epic = new Epic("Эпик 1", "Описание эпика 2");
        taskManager.createEpic(epic);
        Subtask subTask = new Subtask("Подзадача  1", "Описание подзадачи  3",
                epic.getId(), time3, 60);
        taskManager.createSubTask(subTask);

        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);

        httpTaskServer.start();


    }
}