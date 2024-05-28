package tracker.model;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> idSubTasks = new ArrayList<>();


    public Epic(String title, String description) {
        super(title, description);
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
                ", Статус задачи = " + status +
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
}
