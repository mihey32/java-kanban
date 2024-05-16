public class SubTask extends Task {
    Integer idEpic;


    public SubTask(String title, String description, Integer idEpic) {
        super(title, description);
        this.idEpic = idEpic;
    }

    public SubTask(Integer id, String title, String description, Status status, Integer idEpic) {
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
}
