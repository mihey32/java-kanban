package tracker;

import tracker.controllers.Managers;
import tracker.controllers.TaskManager;
import tracker.model.Status;
import tracker.model.Subtask;
import tracker.model.Epic;
import tracker.model.Task;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = new Managers().inMemoryTaskManager();

        //Иницилизируем и создаем задачи:
        Task task1 = new Task("Задача 1", "Описание1");
        Task task2 = new Task("Задача 1", "Описание1");
        manager.createTask(task1);
        manager.createTask(task2);
        Task task3 = new Task(task1.getId(),"Обновленная Задача 1", " Обновленное Описание1", Status.IN_PROGRESS);
        Task task4 = new Task(task2.getId(),"Обновленная Задача 2", " Обновленное Описание2", Status.IN_PROGRESS);
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        manager.updateTask(task3);
        manager.updateTask(task4);
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());

        System.out.println("История задач:");
        for (Task task: manager.getHistory()) {
            System.out.println(task);
        }

        System.out.println();


        // Иницилизируем и создаем Эпики
        Epic epic1 = new Epic("Эпик 1", "Описание1");
        Epic epic2 = new Epic("Эпик 2", "Описание2");
        Epic epic3 = new Epic("Эпик 3", "Описание3");
        Epic epic4 = new Epic("Эпик 4", "Описание4");
        Epic epic5 = new Epic("Эпик 5", "Описание5");
        Epic epic6 = new Epic("Эпик 6", "Описание6");
        Epic epic7 = new Epic("Эпик 7", "Описание7");
        Epic epic8 = new Epic("Эпик 8", "Описание8");
        Epic epic9 = new Epic("Эпик 9", "Описание9");
        Epic epic10 = new Epic("Эпик 10", "Описание10");
        Epic epic11 = new Epic("Эпик 11", "Описание11");

        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createEpic(epic3);
        manager.createEpic(epic4);
        manager.createEpic(epic5);
        manager.createEpic(epic6);
        manager.createEpic(epic7);
        manager.createEpic(epic8);
        manager.createEpic(epic9);
        manager.createEpic(epic10);
        manager.createEpic(epic11);

        //Иницилизируем и создаем Подзадачи
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание1", epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание2", epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание3", epic2.getId());

        manager.createSubTask(subtask1);
        manager.createSubTask(subtask2);
        manager.createSubTask(subtask3);

        manager.getEpicById(4);
        manager.getEpicById(5);
        manager.getEpicById(6);
        manager.getEpicById(7);
        manager.getEpicById(8);
        manager.getEpicById(9);
        manager.getEpicById(10);
        manager.getEpicById(11);
        manager.getEpicById(12);
        manager.getEpicById(13);
        manager.getEpicById(4);
        manager.getEpicById(5);
        manager.getEpicById(6);
        manager.getEpicById(7);
        manager.getEpicById(8);

        printAllTasks(manager);

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




