package tracker.model;

public class Subtask extends Task {
    private Integer idEpic;


    public Subtask(String title, String description, Integer idEpic) {
        super(title, description);
        this.idEpic = idEpic;
    }

    public Subtask(Integer id, String title, String description, Status status, Integer idEpic) {
        super(id, title, description, status);
        this.idEpic = idEpic;
    }


    @Override
    public String toString() {
        return "Подзадача {" +
                "Название ='" + title + '\'' +
                ", Описание подзадачи ='" + description + '\'' +
                ", tracker.model.Task ID = " + id +
                ", Статус задачи = " + status +
                '}';
    }

    public Integer getIdEpic() {
        return idEpic;
    }
}
