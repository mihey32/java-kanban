package tracker.controllers;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface TaskManager {
    ArrayList<Epic> getEpics();

    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEic();

    void deleteTaskById(Integer requiredId);

    void deleteEpicById(Integer requiredId);

    void deleteSubtaskById(Integer requiredId);

    Task getTaskById(Integer id);

    Epic getEpicById(Integer id);

    Subtask getSubtaskById(Integer id);

    void createTask(Task newTask);

    void createEpic(Epic newEpic);

    void createSubTask(Subtask newSubtask);

    void updateEpicStatus(Integer epicId);

    void updateSubTask(Subtask newSubtask);

    void updateTask(Task newTask);

    void updateEpic(Epic newTask);

    ArrayList<Subtask> getSubtasksOfEpic(Epic epic);

    ArrayList<Task> getHistory();

    List<Task> getPrioritizedTasks();
}
