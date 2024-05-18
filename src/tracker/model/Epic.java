package tracker.model;

import java.util.ArrayList;

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
                ", tracker.model.Epic ID  = " + id +
                ", Количество подзадач: " + idSubTasks.size() +
                ", Статус задачи = " + status +
                '}';
    }
}
