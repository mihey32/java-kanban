package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tracker.model.Subtask;
import tracker.enums.TaskType;

class SubtaskTest {
    @Test
    void testEqualsSubtask() {
        Subtask subtask1 = new Subtask("Test1 title", "Test1 description", 1);
        Subtask subtask2 = new Subtask("Test1 title", "Test1 description", 1);

        Assertions.assertEquals(subtask1, subtask2);

    }

    @Test
    void testSubtaskTaskType() {
        Subtask subtaskTest = new Subtask("Test1 title", "Test1 description", 1);
        Assertions.assertEquals(TaskType.SUBTASK, subtaskTest.getTaskType());
    }

    @Test
    void testSubtaskGetIdEpic() {
        int idEpic = 1;
        Subtask subtask1 = new Subtask("Test1 title", "Test1 description", idEpic);
        Assertions.assertEquals(idEpic, subtask1.getIdEpic());
    }

}