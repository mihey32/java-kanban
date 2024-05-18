package tracker;

import tracker.controllers.TaskManager;
import tracker.model.Subtask;
import tracker.model.Epic;
import tracker.model.Task;
import tracker.model.Status;


public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        //Иницилизируем и создаем задачи:
        Task task1 = new Task("Задача 1", "Описание1");
        Task task2 = new Task("Задача 2", "Описание2");
        taskManager.createTask(task1);
        taskManager.createTask(task2);


        // Иницилизируем и создаем Эпики
        Epic epic1 = new Epic("Эпик 1", "Описание1");
        Epic epic2 = new Epic("Эпик 2", "Описание2");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        //Иницилизируем и создаем Подзадачи
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание1", epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание2", epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание3", epic2.getId());

        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);
        taskManager.createSubTask(subtask3);



        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());


        Task task3 = new Task(task1.getId(), "Обновленная Задача 1", "Новое описание 1", Status.DONE);
        Task task4 = new Task(task2.getId(), "Обновленная Задача 2", "Новое описание 2", Status.IN_PROGRESS);

        taskManager.updateTask(task3);
        taskManager.updateTask(task4);

        Subtask subtask4 = new Subtask(subtask1.getId(), "Обновленная Подзадача 1", "Новое Описание1", Status.IN_PROGRESS ,epic1.getId());
        Subtask subtask5 = new Subtask(subtask1.getId(), "Обновленная Подзадача 2", "Новое Описание2", Status.DONE, epic1.getId());
        Subtask subtask6 = new Subtask(subtask3.getId(), "Обновленная Подзадача 3", "Новое Описание3", Status.DONE, epic2.getId());

        taskManager.updateSubTask(subtask4);
        taskManager.updateSubTask(subtask5);
        taskManager.updateSubTask(subtask6);


        System.out.println();

        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());

        System.out.println();

        taskManager.deleteTaskById(1);
        taskManager.deleteEpicById(3);
        taskManager.deleteSubtaskById(7);

        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());


    }
}
