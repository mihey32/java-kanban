package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.enums.Status;
import tracker.model.Task;
import tracker.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

class TaskTest {

    private Task task;
    private LocalDateTime startTime;
    private Duration duration;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm");

    @Test
    public void testEqualsTask() {
        Task task1 = new Task("Test1 title", "Test1 description");
        Task task2 = new Task("Test1 title", "Test1 description");
        Assertions.assertEquals(task1, task2);

    }

    @Test
    void testTaskType() {
        Task taskTest = new Task("Test1 title", "Test1 description");
        Assertions.assertEquals(TaskType.TASK, taskTest.getTaskType());
    }

    @BeforeEach
    void beforeEach() {
        duration = Duration.ofMinutes(60);
        startTime = LocalDateTime.of(2024, Month.SEPTEMBER, 1, 12, 0, 0);
        task = new Task(1, "Test title", "Test description", Status.NEW, startTime, duration);
    }

    @Test
    void testTaskGetEndTime() {
        LocalDateTime startTimeTest = LocalDateTime.of(2024, Month.SEPTEMBER, 1, 12, 0, 0);
        Duration durationTest = Duration.ofMinutes(60);
        LocalDateTime endTimeTest = startTimeTest.plus(durationTest);
        Assertions.assertEquals(endTimeTest, task.getEndTime());
    }
    @Test
    void testTaskGetEndTimeString() {
        LocalDateTime startTimeTest = LocalDateTime.of(2024, Month.SEPTEMBER, 1, 12, 0, 0);
        Duration durationTest = Duration.ofMinutes(60);
        LocalDateTime endTimeTest = startTimeTest.plus(durationTest);
        String endTimeTestString = endTimeTest.format(FORMATTER);
        Assertions.assertEquals(endTimeTestString, task.getEndTimeString());
    }

    @Test
    void testTaskGetStartTime() {
        Assertions.assertEquals(startTime, task.getStartTime());
    }

     @Test
    void testTaskGetStartTimeString() {
        String startTimeString =  startTime.format(FORMATTER);
        Assertions.assertEquals(startTimeString, task.getStartTimeString());
    }

    @Test
    void testTaskGetDuration() {
        int durationTest = 60;
        Assertions.assertEquals(durationTest, task.getDuration());
    }

    @Test
    void testTaskSetDuration() {
        int newDuration = 80;
        task.setDuration(newDuration);
        Assertions.assertEquals(newDuration, task.getDuration());

    }


}