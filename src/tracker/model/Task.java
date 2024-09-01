package tracker.model;

import tracker.enums.Status;
import tracker.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    protected String title; // заголовок задачи
    protected String description; //описание задачи

    protected Integer id; // идентификатор задачи

    protected Status status = Status.NEW; // статус задачи

    protected LocalDateTime startTime;  //время начала задачи
    protected Duration duration = Duration.ZERO;  //продолжительность задачи

    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm");


    public Task(String title, String description) { // для создания задачи
        this.title = title;
        this.description = description;
    }

    public Task(Integer id, String title, String description, Status status) { //  для обновления
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(Integer id, String title, String description, Status status, LocalDateTime startTime,
                Duration duration) { //  для обновления
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String title, String description, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Задача {" +
                "Название ='" + title + '\'' +
                ", Описание задачи ='" + description + '\'' +
                ", Task ID = " + id +
                ", Статус задачи = " + status +
                ", Старт задачи = " + startTime +
                ", Продолжительность задачи = " + duration.toHours() + " ч. " + duration.toMinutesPart() + " мин. " +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return Objects.equals(title, task.title) && Objects.equals(description, task.description) && Objects.equals(id, task.id) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status);
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plus(duration);
        } else {
            return null;
        }
    }

    public void setDuration(int duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return (int) duration.toMinutes();
    }

    public LocalDateTime getStartTime() {
        if (startTime != null) {
            return startTime;
        } else {
            return null;
        }
    }

    public String getStartTimeString() {
        if (startTime != null) {
            return startTime.format(FORMATTER);
        } else {
            return "null";
        }
    }

    public String getEndTimeString() {
        if (startTime != null) {
            return startTime.plus(duration).format(FORMATTER);
        } else {
            return "null";
        }
    }
}
