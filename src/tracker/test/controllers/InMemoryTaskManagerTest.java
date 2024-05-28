package controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.controllers.TaskManager;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.Subtask;
import tracker.model.Task;

import static tracker.controllers.Managers.getDefault;

class InMemoryTaskManagerTest {

    TaskManager manager = getDefault();

    @BeforeEach
    void beForEach() {

        Task task1 = new Task("Задача 1", "Описание1");
        Task task2 = new Task("Задача 1", "Описание1");

        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание1");
        Epic epic2 = new Epic("Эпик 2", "Описание2");

        manager.createEpic(epic1);
        manager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание1", epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание2", epic1.getId());

        manager.createSubTask(subtask1);
        manager.createSubTask(subtask2);

    }


    @Test
    void testTasksEqualityById() {
        Task testTask = new Task("Тестовая Задача", "Тестовое описание");
        manager.createTask(testTask);

        Assertions.assertEquals(testTask, manager.getTaskById(testTask.getId()));

    }

    @Test
    void testEpicEqualityById() {
        Epic testEpic = new Epic("Тестовый эпик", "Тестовое описание");
        manager.createTask(testEpic);
        Assertions.assertEquals(testEpic, manager.getTaskById(testEpic.getId()));
    }

    @Test
    void testSubtaskEqualityById() {
        Epic testEpic = new Epic("Тестовый эпик", "Тестовое описание");
        manager.createTask(testEpic);

        Subtask subtask = new Subtask("Тестовая подзадача", "Тестовое описание", testEpic.getId());

        manager.createTask(subtask);
        Assertions.assertEquals(subtask, manager.getTaskById(subtask.getId()));
    }

    @Test
    void testDeleteAllTasks() {
        manager.deleteAllTasks();

        Assertions.assertEquals(0, manager.getTasks().size());

    }

    @Test
    void testDeleteAllEpic() {
        manager.deleteAllEic();
        Assertions.assertEquals(0, manager.getEpics().size());
        Assertions.assertEquals(0, manager.getSubtasks().size());
    }

    @Test
    void testDeleteAllSubtask() {
        manager.deleteAllEic();
        Assertions.assertEquals(0, manager.getSubtasks().size());
    }

    @Test
    void testInvariabilityTask() {
        Task checkTas = new Task("Тестовая Задача", "Тестовое описание");
        manager.createTask(checkTas);

        Task testTask = manager.getTaskById(checkTas.getId());
        Assertions.assertEquals(checkTas, testTask);

    }

    @Test
    void checkForIdConflicts() {
        Task testTask1 = new Task("Тестовая Задача 1", "Тестовое описание 1");
        Task testTask2 = new Task("Тестовая Задача 2", "Тестовое описание 2");

        manager.createTask(testTask1);
        manager.createTask(testTask2);

        Assertions.assertNotEquals(manager.getTaskById(testTask1.getId()), manager.getTaskById(testTask2.getId()));

    }

    @Test
    void testInsertTasksOfDifferentTypes() {
        Task checkTask = new Task("Контрольная Задача ", "Описание");
        Epic checkEpic = new Epic("Контрольный Эпик ", "Описание");
        manager.createTask(checkTask);
        manager.createEpic(checkEpic);
        Subtask checkSubtask = new Subtask("Контрольная Подзадача", "Описание", checkEpic.getId());

        manager.createSubTask(checkSubtask);

        Assertions.assertNotEquals(0, manager.getTasks().size());
        Assertions.assertNotEquals(0, manager.getEpics().size());
        Assertions.assertNotEquals(0, manager.getSubtasks().size());

        Assertions.assertEquals(checkTask, manager.getTaskById(checkTask.getId()));
        Assertions.assertEquals(checkEpic, manager.getEpicById(checkEpic.getId()));
        Assertions.assertEquals(checkSubtask, manager.getSubtaskById(checkSubtask.getId()));

    }

    @Test
    void checkHistoryManagerSavesTaskVersions(){
        Task chekTask = new Task("Тестовая Задача 1", "Тестовое описание 1");
        manager.createTask(chekTask);
        manager.getTaskById(chekTask.getId());
        Task testTask = new Task(chekTask.getId(),"Тестовая Задача 1", "Тестовое описание 1", Status.IN_PROGRESS);
        manager.updateTask(testTask);
        manager.getTaskById(chekTask.getId());
        Assertions.assertEquals(chekTask, manager.getHistory().getFirst());

    }

}