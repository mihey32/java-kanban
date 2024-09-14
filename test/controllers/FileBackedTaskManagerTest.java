package controllers;

import org.junit.jupiter.api.Test;
import tracker.controllers.FileBackedTaskManager;
import tracker.controllers.TaskManager;
import tracker.exception.ManagerSaveException;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class FileBackedTaskManagerTest {
    private File file;
    private TaskManager manager;
    private FileBackedTaskManager backedTaskManager;


    @Test
    void checkLoadFromFile() {
        try {
            file = File.createTempFile("test", ".csv");
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
        manager = new FileBackedTaskManager(file);

        Task task = new Task("Задача", "Описание задачи");
        Epic epic = new Epic("Эпик", "Описание эпика");

        manager.createTask(task);
        manager.createEpic(epic);

        Subtask subtask = new Subtask("Подзадача", "Описание подзадачи", epic.getId());
        manager.createSubTask(subtask);
        backedTaskManager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(1, backedTaskManager.getTasks().size());
        assertEquals(manager.getTasks(), backedTaskManager.getTasks());

        assertEquals(1, backedTaskManager.getEpics().size());
        assertEquals(manager.getEpics(), backedTaskManager.getEpics());

        assertEquals(1, backedTaskManager.getSubtasks().size());
        assertEquals(manager.getSubtasks(), backedTaskManager.getSubtasks());

    }

    @Test
    void testSaveAndLoadEmptyFile() {
        try {
            file = File.createTempFile("test", ".csv");
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
        manager = new FileBackedTaskManager(file);

        backedTaskManager = FileBackedTaskManager.loadFromFile(file);

    }


}
