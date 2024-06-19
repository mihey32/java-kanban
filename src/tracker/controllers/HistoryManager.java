package tracker.controllers;

import tracker.model.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);


    ArrayList<Task> getHistory();


}
