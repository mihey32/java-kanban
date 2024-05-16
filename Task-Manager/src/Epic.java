import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> idSubTasks = new ArrayList<>();


    public Epic(String title, String description) {
        super(title, description);
    }

    public Epic(Integer id, String title, String description, Status status) {
        super(id, title, description, status);
    }


    public ArrayList<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    public void setIdSubTasks(ArrayList<Integer> idSubTasks) {
        this.idSubTasks = idSubTasks;
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
}
