package tracker.controllers;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.ArrayList;

public class Managers {


    public TaskManager getDefault() {
        return new TaskManager() {
            @Override
            public ArrayList<Epic> getEpics() {
                return null;
            }

            @Override
            public ArrayList<Task> getTasks() {
                return null;
            }

            @Override
            public ArrayList<Subtask> getSubtasks() {
                return null;
            }

            @Override
            public void deleteAllTasks() {

            }

            @Override
            public void deleteAllSubtasks() {

            }

            @Override
            public void deleteAllEic() {

            }

            @Override
            public void deleteTaskById(Integer requiredId) {

            }

            @Override
            public void deleteEpicById(Integer requiredId) {

            }

            @Override
            public void deleteSubtaskById(Integer requiredId) {

            }

            @Override
            public Task getTaskById(Integer id) {
                return null;
            }

            @Override
            public Epic getEpicById(Integer id) {
                return null;
            }

            @Override
            public Subtask getSubtaskById(Integer id) {
                return null;
            }

            @Override
            public void createTask(Task newTask) {

            }

            @Override
            public void createEpic(Epic newEpic) {

            }

            @Override
            public void createSubTask(Subtask newSubtask) {

            }

            @Override
            public void updateEpicStatus(Integer epicId) {

            }

            @Override
            public void updateSubTask(Subtask newSubtask) {

            }

            @Override
            public void updateTask(Task newTask) {

            }

            @Override
            public void updateEpic(Epic newTask) {

            }

            @Override
            public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
                return null;
            }

            @Override
            public ArrayList<Task> getHistory() {
                return null;
            }
        };
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


    public TaskManager inMemoryTaskManager() {
        return new InMemoryTaskManager();
    }
}
