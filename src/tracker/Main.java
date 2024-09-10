package tracker;

import tracker.controllers.FileBackedTaskManager;
import tracker.controllers.TaskManager;
import tracker.model.Epic;
import tracker.enums.Status;
import tracker.model.Subtask;
import tracker.model.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class Main {
    public static void main(String[] args) {

        File file = new File("resources/backup.csv");

        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        LocalDateTime time3 = LocalDateTime.of(2021, Month.JANUARY, 8, 1, 0, 0);
        LocalDateTime time4 = LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0, 0);
        Duration duration4 = Duration.ofHours(1);
        Duration duration3 = Duration.ofHours(2);

        //Иницилизируем и создаем задачи:
        Task task1 = new Task("Задача 1", "Описание1");
        Task task2 = new Task("Задача 2", "Описание2");
        manager.createTask(task1);
        manager.createTask(task2);

        Task task3 = new Task(task1.getId(),
                "Обновленная Задача 1",
                " Обновленное Описание1",
                Status.IN_PROGRESS, time3, duration3);
        Task task4 = new Task(task2.getId(),
                "Обновленная Задача 2",
                " Обновленное Описание2",
                Status.IN_PROGRESS, time4, duration4);
        manager.updateTask(task3);

        manager.updateTask(task4);

        Epic epic1 = new Epic("Эпик 1", "Описание1");
        Epic epic2 = new Epic("Эпик 2", "Описание2");

        manager.createEpic(epic1);
        manager.createEpic(epic2);

        LocalDateTime time1 = LocalDateTime.of(2020, Month.JANUARY, 4, 1, 0, 0);
        LocalDateTime time2 = LocalDateTime.of(2020, Month.JANUARY, 18, 0, 0, 0);
        Duration duration1 = Duration.ofHours(1);
        Duration duration2 = Duration.ofHours(2);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание1", epic1.getId(), time1, duration1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание2", epic1.getId(), time2, duration2);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание3", epic2.getId());
        manager.createSubTask(subtask1);
        manager.createSubTask(subtask2);
        manager.createSubTask(subtask3);

        Subtask subtask4 = new Subtask(subtask3.getId(),
                "Обновленная подзадача3",
                "Обвновленное описание3",
                Status.DONE,
                subtask3.getIdEpic());
        manager.updateSubTask(subtask4);

        Subtask subtask5 = new Subtask(subtask2.getId(),
                "Обновленная подзадача2",
                "Описание2",
                Status.IN_PROGRESS, time2, duration2, epic1.getId());
        manager.updateSubTask(subtask5);
     manager.deleteSubtaskById(subtask1.getId());

        printAllTasks(manager);

        System.out.println("----------------------");

        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        printAllTasks(fileBackedTaskManager);

        System.out.println("*************");
        printPrioritizedTasks(manager);


    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getSubtasksOfEpic(epic)) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

    private static void printPrioritizedTasks(TaskManager manager) {

        int countTask = manager.getTasks().size() + manager.getSubtasks().size();
        System.out.println("Количество задач и подзадач: " + countTask);
        System.out.println("");
        System.out.println("Количество задач в списке с приоритетами: " + manager.getPrioritizedTasks().size());

        for (int i = 0; i < manager.getPrioritizedTasks().size(); i++) {
            Task task = manager.getPrioritizedTasks().get(i);
            System.out.println("Приоритет " + (i + 1) + " - " + task.toString());
        }
    }

}