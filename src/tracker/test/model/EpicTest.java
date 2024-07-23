package model;

import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.TaskType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    @Test
    void testEqualsEpic(){

        Epic epic1 = new Epic("Test1 title", "Test1 description");
        Epic epic2= new Epic("Test1 title", "Test1 description");

        assertEquals(epic1, epic2);

    }

    @Test
    void testEpicTaskType() {
        Epic epicTest = new Epic("Test1 title", "Test1 description");
        assertEquals(TaskType.EPIC, epicTest.getTaskType());
    }

}