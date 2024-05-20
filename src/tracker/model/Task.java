package tracker.model;

public class Task {
    protected  String title; // заголовок задачи
    protected  String description; //описание задачи

    protected  Integer id; // идентификатор задачи

    protected Status status; // статус задачи


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
                ", Task ID = " + id +
                ", Статус задачи = " + status +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
