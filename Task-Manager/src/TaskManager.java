import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    public Map<Integer, Task> task = new HashMap<>();
    public Map<Integer, Task> epic = new HashMap<>();
    public Map<Integer, Task> subTask = new HashMap<>();
    private Integer id = 1;

    public Map<Integer, Task> getEpic() {
        return epic;
    }

    public void getListTasks() { //метод печает список всех задач
        for (Task task : task.values()) {
            System.out.println(task.toString());
        }
        for (Task epic : epic.values()) {
            System.out.println(epic.toString());
        }
        for (Task subTask : subTask.values()) {
            System.out.println(subTask.toString());
        }
    }

    public void deleteAllTasks() { //удалить все задачи
        task.clear();
        epic.clear();
        subTask.clear();
        System.out.println("Все задачи удалены!");
    }

     public void deleteTaskById(Integer requiredId) { // удалить задачу по requiredId
        if (requiredId > id) {
            System.out.println("Задача с ID '" + requiredId + "' не существует");
        } else if (requiredId < 0) {
            System.out.println("ID не может быть отрицательным или равным нулю");
        } else {
            task.remove(requiredId);

            subTask.remove(requiredId);

            if (epic.get(requiredId) != null) {
                Epic epic1 = (Epic) epic.get(requiredId);
                if (epic1.idSubTasks != null) {
                    for (Integer idSub : epic1.getIdSubTasks()) {
                        subTask.remove(idSub);
                    }
                    epic.remove(requiredId);

                } else {
                    epic.remove(requiredId);
                }
            }
            System.out.println("Задача ID- " + requiredId + " удалена ");
        }
    }

    public void getTaskById(Integer id) { //получить задачу по id
        if (task.get(id) != null) {
            System.out.println(task.get(id).toString());
        } else if (epic.get(id) != null) {
            System.out.println(epic.get(id).toString());
        } else if (subTask.get(id) != null) {
            System.out.println(subTask.get(id).toString());
        } else {
            System.out.println("Задача с ID '" + id + "' не обнаружена");
        }
    }

    public void createTask(Task newTask) { //создать задачу
        newTask.setId(id++);
        newTask.setStatus(Status.NEW);
        task.put(newTask.getId(), newTask);
    }

    public void createEpic(Epic newEpic) { //создать epic
        newEpic.setId(id++);
        newEpic.setStatus(Status.NEW);
        epic.put(newEpic.getId(), newEpic);

    }

    public void createSubTask(SubTask newSubTask) { //создать подзадачу
        if (getEpic().containsKey(newSubTask.idEpic)) {
            newSubTask.setId(id++);
            newSubTask.setStatus(Status.NEW);
            subTask.put(newSubTask.getId(), newSubTask);
            Epic epic1 = (Epic) epic.get(newSubTask.idEpic);
            ArrayList<Integer> arrayList = epic1.getIdSubTasks();
            arrayList.add(newSubTask.id);
            epic1.setIdSubTasks(arrayList);
        } else {
            System.out.println("Невозможно создать подзадачу. Эпика с ID " + newSubTask.idEpic + " нет");
        }
    }

    public void updateEpicStatus(Integer epicId) {  //метод для обновления статуса эпика
        if (epic.get(epicId) != null) {
            Epic epic1 = (Epic) epic.get(epicId);
            ArrayList<Integer> arrayList = epic1.getIdSubTasks();

            int countNew = 0;
            int countDone = 0;

            for (Integer id : arrayList) {
                Status status = subTask.get(id).getStatus();
                if (status == Status.NEW) {
                    countNew++;
                } else if (status == Status.DONE) {
                    countDone++;
                }
            }
            if (countNew == arrayList.size()) {
                epic1.setStatus(Status.NEW);
            } else if (countDone == arrayList.size()) {
                epic1.setStatus(Status.DONE);
            } else {
                epic1.setStatus(Status.IN_PROGRESS);
            }
        } else {
            System.out.println("Что-то пошло не так");
        }
    }

    public void updateSubTask(SubTask newSubTask) { //обновить подзадачу
        subTask.put(newSubTask.getId(), newSubTask);
        SubTask subTask1 = (SubTask) subTask.get(newSubTask.getId());
        Integer idEpic = subTask1.getIdEpic();
        updateEpicStatus(idEpic);
    }

    public void updateTask(Task newTask) { // обновить задачу
        task.put(newTask.getId(), newTask);
    }

    public void updateEpic(Epic newTask) { // обновить Эпик
        epic.put(newTask.getId(), newTask);

    }

    public void getSubtasksOfEpic(Epic epic) { //Получение списка всех подзадач определённого эпика.
        if (this.epic.containsValue(epic)) {
            if (!epic.getIdSubTasks().isEmpty()) {
                System.out.println("Выводим список подзадач эпика ...");
                for (Integer idSubTask : epic.getIdSubTasks()) {
                    System.out.println(subTask.get(idSubTask).toString());
                }
            } else {
                System.out.println("Данный эпик не содержит подзадач!");
            }
        } else {
            System.out.println("Данный эпик отсутствует в списке задач!");
        }

    }
}
