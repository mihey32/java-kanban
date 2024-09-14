package tracker.model;

import tracker.enums.Status;
import tracker.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> idSubTasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description);
    }

    public Epic(Integer id, String title, String description, Status status) {
        super(id, title, description, status);
    }

    public Epic(Integer id, String title, String description, Status status, LocalDateTime startTime,
                long duration, LocalDateTime endTime) {
        super(id, title, description, status, startTime, duration);
        this.endTime = endTime;
    }


    public ArrayList<Integer> getIdSubtasks() {
        return idSubTasks;
    }

    public void setIdSubtasks(ArrayList<Integer> idSubTasks) {
        this.idSubTasks = idSubTasks;
    }

    public void cleanSubtaskIds() {
        idSubTasks.clear();
    }

    @Override
    public String toString() {
        return "Эпик {" +
                "Название ='" + title + '\'' +
                ", Описание эпика ='" + description + '\'' +
                ", Epic ID  = " + id +
                ", Количество подзадач: " + idSubTasks.size() +
                ", Статус эпика = " + status +
                ", Cтарт эпика = " + startTime +
                ", Продолжительность эпика = " + duration + " мин. " +
                ", Окончание эпика = " + endTime +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Epic epic = (Epic) object;
        return Objects.equals(idSubTasks, epic.idSubTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idSubTasks);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getEndTimeEpicString() {
        if (endTime != null) {
            return endTime.format(FORMATTER);
        } else {
            return "null";
        }
    }
}
