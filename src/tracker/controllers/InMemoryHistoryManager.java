package tracker.controllers;

import tracker.model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> tasksHistory = new ArrayList<>();
    private final int HISTORY_COUNT = 10;


    @Override
    public void add(Task task) {
        tasksHistory.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        while (tasksHistory.size() > HISTORY_COUNT) {
            tasksHistory.removeFirst();
        }
        return tasksHistory;
    }
}
