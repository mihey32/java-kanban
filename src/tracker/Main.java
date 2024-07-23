package tracker;

import tracker.controllers.FileBackedTaskManager;
import tracker.controllers.TaskManager;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.Subtask;
import tracker.model.Task;

import java.io.File;
import java.io.IOException;

import static tracker.controllers.Managers.getDefault;

public class Main {
    public static void main(String[] args) {

        // TaskManager manager = getDefault();
       File file = new File("resources/backup.csv");

        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        //Иницилизируем и создаем задачи:
        Task task1 = new Task("Задача 1", "Описание1");
        Task task2 = new Task("Задача 2", "Описание2");
        manager.createTask(task1);
        manager.createTask(task2);

        Task task3 = new Task(task1.getId(),
                "Обновленная Задача 1",
                " Обновленное Описание1",
                Status.IN_PROGRESS);
        Task task4 = new Task(task2.getId(),
                "Обновленная Задача 2",
                " Обновленное Описание2",
                Status.IN_PROGRESS);
        manager.updateTask(task3);
        manager.updateTask(task4);

        Epic epic1 = new Epic("Эпик 1", "Описание1");
        Epic epic2 = new Epic("Эпик 2", "Описание2");

        manager.createEpic(epic1);
        manager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание1", epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание2", epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание3", epic2.getId());
        manager.createSubTask(subtask1);
        manager.createSubTask(subtask2);
        manager.createSubTask(subtask3);

        Subtask subtask4 = new Subtask(subtask1.getId(),
                "Обновленная подзадача1",
                "Обвновленное описание1",
                Status.DONE,
                epic1.getId());
        manager.updateSubTask(subtask4);

        printAllTasks(manager);

        System.out.println("----------------------");

        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        printAllTasks(fileBackedTaskManager);

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
}