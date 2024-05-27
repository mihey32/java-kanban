package tracker.model;

import java.util.Objects;

public class Task {
    protected  String title; // заголовок задачи
    protected  String description; //описание задачи

    protected  Integer id; // идентификатор задачи

    protected Status status; // статус задачи

    public Task(Integer id, String title, String description) {

    }

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
                ", tracker.model.Task ID = " + id +
                ", Статус задачи = " + status +
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
}
