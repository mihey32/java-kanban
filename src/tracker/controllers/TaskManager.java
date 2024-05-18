package tracker.controllers;

import tracker.model.Subtask;
import tracker.model.Epic;
import tracker.model.Task;
import tracker.model.Status;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {


    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Task> epics = new HashMap<>();
    private Map<Integer, Task> subtasks = new HashMap<>();
    private Integer id = 1;

    public ArrayList<Epic> getEpics() {
        ArrayList<Task> tasks = new ArrayList<>(epics.values());
        ArrayList<Epic> epics = new ArrayList<>();
        for (Task task : tasks) {
            epics.add((Epic) task);
        }
        return epics;
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<Task>(tasks.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Task> tasks = new ArrayList<>(subtasks.values());
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Task task : tasks) {
            subtasks.add((Subtask) task);
        }
        return subtasks;
    }

    public void deleteAllTasks() { //удалить все задачи
        tasks.clear();
    }

    public void deleteAllSubtasks() { //удалить все подзадачи

        for (Task task : epics.values()) {
            Epic epic = (Epic) task;
            epic.cleanSubtaskIds();
            updateEpicStatus(epic.getId());
        }
        subtasks.clear();
    }

    public void deleteAllEic() { //удалить все эпики
        epics.clear();
        subtasks.clear();
    }

    public void deleteTaskById(Integer requiredId) {  // удалить задачу по requiredId
        tasks.remove(requiredId);
    }

    public void deleteEpicById(Integer requiredId) { // удалить эпик по requiredId
        Epic epic = (Epic) epics.get(requiredId);

        if (epic.getIdSubtasks().isEmpty()) {
            epics.remove(requiredId);
        } else {
            for (Integer id : epic.getIdSubtasks()) {
                subtasks.remove(id);
            }
            epics.remove(requiredId);
        }
    }

    public void deleteSubtaskById(Integer requiredId) { // удалить подзадачу по requiredId
        Subtask subtask = (Subtask) subtasks.get(requiredId);
        Integer idEpic = subtask.getIdEpic();
        Epic epic = (Epic) epics.get(idEpic);
        ArrayList<Integer> idSubtaskList = epic.getIdSubtasks();
        idSubtaskList.remove(requiredId);
        epic.setIdSubtasks(idSubtaskList);

        subtasks.remove(requiredId);
        updateEpicStatus(idEpic);
    }

    public Task getTaskById(Integer id) { //получить задачу по id
        return tasks.get(id);
    }

    public Epic getEpicById(Integer id) { //получить эпик по id
        return (Epic) epics.get(id);
    }

    public Subtask getSubtaskById(Integer id) { //получить подзадачу по id
        return (Subtask) subtasks.get(id);
    }

    public void createTask(Task newTask) { //создать задачу
        newTask.setId(id++);
        newTask.setStatus(Status.NEW);
        tasks.put(newTask.getId(), newTask);
    }

    public void createEpic(Epic newEpic) { //создать epic
        newEpic.setId(id++);
        newEpic.setStatus(Status.NEW);
        epics.put(newEpic.getId(), newEpic);

    }

    public void createSubTask(Subtask newSubtask) { //создать подзадачу
        newSubtask.setId(id++);
        newSubtask.setStatus(Status.NEW);
        subtasks.put(newSubtask.getId(), newSubtask);

        Epic epic = (Epic) epics.get(newSubtask.getIdEpic());
        ArrayList<Integer> idSubtask = epic.getIdSubtasks();
        idSubtask.add(newSubtask.getId());
        epic.setIdSubtasks(idSubtask);
    }


    public void updateEpicStatus(Integer epicId) {  //метод для обновления статуса эпика
        Epic epic = (Epic) epics.get(epicId);

        int countNew = 0;
        int countDone = 0;
        ArrayList<Integer> idSubTasks = epic.getIdSubtasks();

        if (idSubTasks.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            for (Integer id : idSubTasks) {
                Subtask subtask = (Subtask) subtasks.get(id);
                Status statusSubtask = subtask.getStatus();
                if (statusSubtask == Status.NEW) {
                    countNew++;
                } else if (statusSubtask == Status.DONE) {
                    countDone++;
                }
            }
            if (countNew == idSubTasks.size()) {
                epic.setStatus(Status.NEW);
            } else if (countDone == idSubTasks.size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }

    }

    public void updateSubTask(Subtask newSubtask) { //обновить подзадачу
        subtasks.put(newSubtask.getId(), newSubtask);

        Subtask subtask = (Subtask) subtasks.get(newSubtask.getId());
        Integer idEpic = subtask.getIdEpic();
        updateEpicStatus(idEpic);
    }

    public void updateTask(Task newTask) { // обновить задачу
        tasks.put(newTask.getId(), newTask);
    }

    public void updateEpic(Epic newTask) { // обновить Эпик
        epics.put(newTask.getId(), newTask);

    }

    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {   //Получение списка всех подзадач определённого эпика.
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        for (Integer id : epic.getIdSubtasks()) {
            Subtask subtask = (Subtask) subtasks.get(id);
            subtasksOfEpic.add(subtask);
        }
        return subtasksOfEpic;


    }


}
