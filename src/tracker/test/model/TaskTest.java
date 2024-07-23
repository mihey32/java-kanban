package model;

import org.junit.jupiter.api.Test;
import tracker.model.Task;
import tracker.model.TaskType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    @Test
    public void testEqualsTask() {
        Task task1 = new Task("Test1 title", "Test1 description");
        Task task2 = new Task("Test1 title", "Test1 description");
        assertEquals(task1, task2);

    }
    @Test
    void testEpicTaskType() {
        Task taskTest = new Task("Test1 title", "Test1 description");
        assertEquals(TaskType.TASK, taskTest.getTaskType());
    }
}