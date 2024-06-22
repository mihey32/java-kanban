package tracker.controllers;

import tracker.model.Node;
import tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class InMemoryHistoryManager implements HistoryManager {

    private static class TaskLinkedList<T> {

        private Node<T> head;
        private Node<T> tail;
        private int size = 0;
        private final HashMap<Integer, Node<T>> idNodeMap = new HashMap<>();


        public void linkLast(T task, int id) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, task, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.setNext(newNode);
            }
            size++;
            idNodeMap.put(id, newNode);
        }

        public ArrayList<T> getTasks() {
            ArrayList<T> allTask = new ArrayList<>();
            Node<T> node = head;
            while (node != null) {
                allTask.add(node.getTask());
                node = node.getNext();
            }
            return allTask;
        }

    }

    private final TaskLinkedList<Task> taskTaskLinkedList = new TaskLinkedList<>();

    @Override
    public void add(Task task) {
        if (taskTaskLinkedList.idNodeMap.containsKey(task.getId())) {
            removeNode(taskTaskLinkedList.idNodeMap.get(task.getId()));
        }
        taskTaskLinkedList.linkLast(task, task.getId());
    }

    @Override
    public void remove(int id) {
        removeNode(taskTaskLinkedList.idNodeMap.get(id));
    }

    public void removeNode(Node<Task> deleteNode) {
        if (taskTaskLinkedList.head == null || deleteNode == null) {
            return;
        }

        if (taskTaskLinkedList.head == deleteNode) {
            taskTaskLinkedList.head = deleteNode.getNext();
        }

        if (deleteNode.getNext() != null) {
            deleteNode.getNext().setPrev(deleteNode.getPrev());
        } else {
            taskTaskLinkedList.tail = deleteNode.getPrev();
        }

        if (deleteNode.getPrev() != null) {
            deleteNode.getPrev().setNext(deleteNode.getNext());
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return taskTaskLinkedList.getTasks();
    }
}
