package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tracker.model.Task;

class TaskTest {

    @Test
    public void testEqualsTask() {
        Task task1 = new Task("Test1 title", "Test1 description");
        Task task2 = new Task("Test1 title", "Test1 description");
        Assertions.assertEquals(task1, task2);

    }




}