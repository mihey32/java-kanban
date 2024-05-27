package tracker;

import tracker.controllers.Managers;
import tracker.controllers.TaskManager;
import tracker.model.Status;
import tracker.model.Subtask;
import tracker.model.Epic;
import tracker.model.Task;

public class Main {
    public static void main(String[] args) {

        TaskManager managers = new Managers().inMemoryTaskManager();

        //Иницилизируем и создаем задачи:
        Task task1 = new Task("Задача 1", "Описание1");
        Task task2 = new Task("Задача 1", "Описание1");
        managers.createTask(task1);
        managers.createTask(task2);
        Task task3 = new Task(task1.getId(),"Обновленная Задача 1", " Обновленное Описание1", Status.IN_PROGRESS);
        Task task4 = new Task(task2.getId(),"Обновленная Задача 2", " Обновленное Описание2", Status.IN_PROGRESS);
        managers.getTaskById(task1.getId());
        managers.getTaskById(task2.getId());
        managers.updateTask(task3);
        managers.updateTask(task4);
        managers.getTaskById(task1.getId());
        managers.getTaskById(task2.getId());

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

        managers.createEpic(epic1);
        managers.createEpic(epic2);
        managers.createEpic(epic3);
        managers.createEpic(epic4);
        managers.createEpic(epic5);
        managers.createEpic(epic6);
        managers.createEpic(epic7);
        managers.createEpic(epic8);
        managers.createEpic(epic9);
        managers.createEpic(epic10);
        managers.createEpic(epic11);

        //Иницилизируем и создаем Подзадачи
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание1", epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание2", epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание3", epic2.getId());

        managers.createSubTask(subtask1);
        managers.createSubTask(subtask2);
        managers.createSubTask(subtask3);

        managers.getEpicById(4);
        managers.getEpicById(5);
        managers.getEpicById(6);
        managers.getEpicById(7);
        managers.getEpicById(8);
        managers.getEpicById(9);
        managers.getEpicById(10);
        managers.getEpicById(11);
        managers.getEpicById(12);
        managers.getEpicById(13);
        managers.getEpicById(4);
        managers.getEpicById(5);
        managers.getEpicById(6);
        managers.getEpicById(7);
        managers.getEpicById(8);

        printAllTasks(managers);

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




