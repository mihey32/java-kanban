package tracker.model;

import java.util.Objects;

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
                ", Task ID = " + id +
                ", Статус задачи = " + status +
                '}';
    }

    public Integer getIdEpic() {
        return idEpic;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Subtask subtask = (Subtask) object;
        return Objects.equals(idEpic, subtask.idEpic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpic);
    }
}
