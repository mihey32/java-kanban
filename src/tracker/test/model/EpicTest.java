package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.TaskType;

class EpicTest {

    @Test
    void testEqualsEpic(){

        Epic epic1 = new Epic("Test1 title", "Test1 description");
        Epic epic2= new Epic("Test1 title", "Test1 description");

        Assertions.assertEquals(epic1, epic2);

    }

    @Test
    void testEpicTaskType() {
        Epic epicTest = new Epic("Test1 title", "Test1 description");
        Assertions.assertEquals(TaskType.EPIC, epicTest.getTaskType());
    }

}