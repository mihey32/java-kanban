package controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.controllers.TaskManager;
import tracker.model.Epic;
import tracker.enums.Status;
import tracker.model.Subtask;
import tracker.model.Task;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static tracker.controllers.Managers.getDefault;

class InMemoryTaskManagerTest {

    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;

    private Subtask subtask1;
    private Subtask subtask2;

    TaskManager manager = getDefault();

    @BeforeEach
    void beForEach() {

        LocalDateTime time1 = LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0);
        LocalDateTime time2 = LocalDateTime.of(2020, Month.JANUARY, 2, 0, 0, 0);

        task1 = new Task("Задача 1", "Описание1", time1, 60);
        task2 = new Task("Задача 1", "Описание1", time2, 60);

        manager.createTask(task1);
        manager.createTask(task2);

        epic1 = new Epic("Эпик 1", "Описание1");

        manager.createEpic(epic1);


        LocalDateTime time3 = LocalDateTime.of(2020, Month.JANUARY, 3, 0, 0, 0);
        LocalDateTime time4 = LocalDateTime.of(2020, Month.JANUARY, 4, 0, 0, 0);


        subtask1 = new Subtask("Подзадача 1", "Описание1", epic1.getId(), time3, 70);
        subtask2 = new Subtask("Подзадача 2", "Описание2", epic1.getId());

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
    void checkHistoryManagerNoSavesTaskVersions() {
        Task chekTask = new Task("Тестовая Задача 1", "Тестовое описание 1");
        manager.createTask(chekTask);
        manager.getTaskById(chekTask.getId());
        Task testTask = new Task(chekTask.getId(), "Тестовая Задача 1", "Тестовое описание 1", Status.IN_PROGRESS);
        manager.updateTask(testTask);
        manager.getTaskById(chekTask.getId());
        Assertions.assertEquals(testTask, manager.getHistory().getFirst());

    }

    @Test
    void checkAddFunctionalityInLinkedList() {
        Task checkTask = new Task("Контрольная Задача ", "Описание");
        Epic checkEpic = new Epic("Контрольный Эпик ", "Описание");
        manager.createTask(checkTask);
        manager.createEpic(checkEpic);
        manager.getEpicById(checkEpic.getId());
        manager.getTaskById(checkTask.getId());

        Assertions.assertEquals(checkTask, manager.getHistory().getLast());
    }

    @Test
    void testRemoveDuplicateTasksFromHistoryList() {
        Task checkTask = new Task("Контрольная Задача ", "Описание");
        Epic checkEpic = new Epic("Контрольный Эпик ", "Описание");
        manager.createTask(checkTask);
        manager.createEpic(checkEpic);
        manager.getEpicById(checkEpic.getId());
        manager.getTaskById(checkTask.getId());
        manager.getEpicById(checkEpic.getId());

        Assertions.assertNotEquals(checkEpic, manager.getHistory().getFirst());
        Assertions.assertEquals(2, manager.getHistory().size());
    }

    @Test
    void testRemoveTaskFromHistoryList() {
        Task checkTask = new Task("Контрольная Задача ", "Описание");
        Epic checkEpic = new Epic("Контрольный Эпик ", "Описание");
        manager.createTask(checkTask);
        manager.createEpic(checkEpic);
        manager.getEpicById(checkEpic.getId());
        manager.getTaskById(checkTask.getId());
        manager.deleteEpicById(checkEpic.getId());

        Assertions.assertEquals(1, manager.getHistory().size());

    }

    @Test
    void testCheckEpicStatus() {
        Epic epicTest = manager.getEpicById(epic1.getId());
        Assertions.assertEquals(Status.NEW, epicTest.getStatus());

        Subtask updateTask1 = new Subtask(subtask1.getId(), "updateTitle1", "updateDescription1",
                Status.DONE, subtask1.getIdEpic());
        manager.updateSubTask(updateTask1);

        Assertions.assertEquals(Status.IN_PROGRESS, epicTest.getStatus());

        Subtask updateTask2 = new Subtask(subtask2.getId(), "updateTitle2", "updateDescription2",
                Status.DONE, subtask2.getIdEpic());
        manager.updateSubTask(updateTask2);

        Assertions.assertEquals(Status.DONE, epicTest.getStatus());

    }

    @Test
    void testGetPrioritizedTasks() {
        List<Task> prioritizedTasks = manager.getPrioritizedTasks();
        Assertions.assertEquals(1, prioritizedTasks.get(0).getId(), "Задача c ID 1 не приоритезирована");
        Assertions.assertEquals(4, prioritizedTasks.get(2).getId(), "Подзадача c ID 4 не приоритезирована");
        Assertions.assertNull(prioritizedTasks.get(3).getStartTime(), "Подзадача c ID 5 не приоритезирована");
    }
    @Test
    void testRemoveTaskFromPrioritizedTasks() {
        int testListSize1 = 4;
        int testListSize2 = 3;
        int testListSize3 = 0;
        Assertions.assertEquals(testListSize1, manager.getPrioritizedTasks().size(), "Неверный размер приоритезированного списка");

        manager.deleteTaskById(1);
        Assertions.assertEquals(testListSize2, manager.getPrioritizedTasks().size(), "Неверный размер приоритезированного списка");

        manager.deleteAllTasks();
        manager.deleteAllSubtasks();
        Assertions.assertEquals(testListSize3, manager.getPrioritizedTasks().size(), "Неверный размер приоритезированного списка");

    }

}