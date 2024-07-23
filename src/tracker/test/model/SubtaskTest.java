package model;

import org.junit.jupiter.api.Test;
import tracker.model.Subtask;
import tracker.model.TaskType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskTest {
    @Test
    void testEqualsSubtask(){

        Subtask subtask1 = new Subtask("Test1 title", "Test1 description",1);
        Subtask subtask2 = new Subtask("Test1 title", "Test1 description",1);

        assertEquals(subtask1, subtask2);

    }

    @Test
    void testSubtaskTaskType() {
        Subtask subtaskTest = new Subtask("Test1 title", "Test1 description",1);
        assertEquals(TaskType.SUBTASK, subtaskTest.getTaskType());
    }

}