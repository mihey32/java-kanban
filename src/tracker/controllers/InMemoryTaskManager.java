package tracker.controllers;

import tracker.enums.Status;
import tracker.enums.TaskType;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {


    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private Integer id = 1;
    static final Comparator<Task> startTimeComparator = Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId);


    protected final Set<Task> prioritizedTasks = new TreeSet<>(startTimeComparator);


    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllTasks() { //удалить все задачи
        tasks.clear();
        prioritizedTasks.removeIf(task -> task.getTaskType() == TaskType.TASK);
    }

    @Override
    public void deleteAllSubtasks() { //удалить все подзадачи
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            updateEpicStatus(epic.getId());
            setEpicStartDataTime(epic.getId());
        }
        subtasks.clear();
        prioritizedTasks.removeIf(task -> task.getTaskType() == TaskType.SUBTASK);

    }

    @Override
    public void deleteAllEic() { //удалить все эпики
        epics.clear();
        subtasks.clear();
        prioritizedTasks.removeIf(task -> task.getTaskType() == TaskType.SUBTASK);
    }

    @Override
    public void deleteTaskById(Integer requiredId) {  // удалить задачу по requiredId
        tasks.remove(requiredId);
        historyManager.remove(requiredId);
        prioritizedTasks.remove(tasks.get(requiredId));
    }

    @Override
    public void deleteEpicById(Integer requiredId) { // удалить эпик по requiredId
        Epic epic = epics.get(requiredId);

        if (epic.getIdSubtasks().isEmpty()) {
            epics.remove(requiredId);
        } else {
            for (Integer id : epic.getIdSubtasks()) {
                subtasks.remove(id);
                prioritizedTasks.remove(subtasks.get(id));
            }
            epics.remove(requiredId);
        }
        historyManager.remove(requiredId);
    }

    @Override
    public void deleteSubtaskById(Integer requiredId) { // удалить подзадачу по requiredId
        Subtask subtask = subtasks.get(requiredId);
        Integer idEpic = subtask.getIdEpic();
        Epic epic = epics.get(idEpic);
        ArrayList<Integer> idSubtaskList = epic.getIdSubtasks();
        idSubtaskList.remove(requiredId);
        epic.setIdSubtasks(idSubtaskList);

        subtasks.remove(requiredId);
        updateEpicStatus(idEpic);
        historyManager.remove(requiredId);
        prioritizedTasks.remove(subtasks.get(requiredId));
    }

    @Override
    public Task getTaskById(Integer id) { //получить задачу по id
        if (tasks.get(id) != null) {
            historyManager.add(tasks.get(id));
        }
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(Integer id) { //получить эпик по id
        if (epics.get(id) != null) {
            historyManager.add(epics.get(id));
        }
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(Integer id) { //получить подзадачу по id
        if (subtasks.get(id) != null) {
            historyManager.add(subtasks.get(id));
        }
        return subtasks.get(id);
    }

    @Override
    public void createTask(Task newTask) { //создать задачу
        try {
            if (!prioritizedTasks.isEmpty()) {
                if (checkTaskIntersection(newTask)) {
                    throw new RuntimeException("Задача \"" + newTask.getTitle() + "\" пересекается с остальными задачами по времени");
                }
            }
            newTask.setId(id++);
            tasks.put(newTask.getId(), newTask);
            prioritizedTasks.add(newTask);

        } catch (RuntimeException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void createEpic(Epic newEpic) { //создать epic
        newEpic.setId(id++);
        epics.put(newEpic.getId(), newEpic);
    }

    @Override
    public void createSubTask(Subtask newSubtask) { //создать подзадачу

        if (!prioritizedTasks.isEmpty()) {
            if (checkTaskIntersection(newSubtask)) {
                throw new RuntimeException("Подзадача \"" + newSubtask.getTitle() +
                        "\" пересекается с остальными задачами по времени");
            }
        }
        newSubtask.setId(id++);
        subtasks.put(newSubtask.getId(), newSubtask);
        Epic epic = epics.get(newSubtask.getIdEpic());
        ArrayList<Integer> idSubtask = epic.getIdSubtasks();
        idSubtask.add(newSubtask.getId());
        epic.setIdSubtasks(idSubtask);
        setEpicStartDataTime(newSubtask.getIdEpic());

        prioritizedTasks.add(newSubtask);

    }


    @Override
    public void updateEpicStatus(Integer epicId) {  //метод для обновления статуса эпика
        Epic epic = epics.get(epicId);

        int countNew = 0;
        int countDone = 0;
        ArrayList<Integer> idSubTasks = epic.getIdSubtasks();

        if (idSubTasks.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            for (Integer id : idSubTasks) {
                Subtask subtask = subtasks.get(id);
                Status statusSubtask = subtask.getStatus();
                if (statusSubtask == Status.NEW) {
                    countNew++;
                } else if (statusSubtask == Status.DONE) {
                    countDone++;
                }
            }
            if (countNew == idSubTasks.size()) {
                epic.setStatus(Status.NEW);
            } else if (countDone == idSubTasks.size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    @Override
    public void updateSubTask(Subtask newSubtask) { //обновить подзадачу
        if (!checkTaskIntersection(newSubtask)) {
            prioritizedTasks.remove(subtasks.get(newSubtask.getId()));
            subtasks.put(newSubtask.getId(), newSubtask);
            prioritizedTasks.add(newSubtask);

            Subtask subtask = subtasks.get(newSubtask.getId());
            Integer idEpic = subtask.getIdEpic();
            updateEpicStatus(idEpic);
        } else {
            throw new RuntimeException("Подзадача \"" + newSubtask.getTitle() + "\" пересекается с остальными задачами по времени");
        }


    }

    @Override
    public void updateTask(Task newTask) { // обновить задачу
        if (!checkTaskIntersection(newTask)) {
            prioritizedTasks.remove(tasks.get(newTask.getId()));
            tasks.put(newTask.getId(), newTask);
            prioritizedTasks.add(newTask);
        } else {
            throw new RuntimeException("Задача \"" + newTask.getTitle() + "\" пересекается с остальными задачами по времени");
        }
    }

    @Override
    public void updateEpic(Epic newTask) { // обновить Эпик
        epics.put(newTask.getId(), newTask);
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {   //Получение списка всех подзадач определённого эпика.
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        for (Integer id : epic.getIdSubtasks()) {
            Subtask subtask = subtasks.get(id);
            subtasksOfEpic.add(subtask);
        }
        return subtasksOfEpic;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public boolean checkTaskIntersection(Task newTask) {
        List<Task> taskList = getPrioritizedTasks();
        for (Task task : taskList) {
            if (task.getStartTime() == null) {
                continue;
            }
            if (newTask.getStartTime() != null &&
                    !(newTask.getEndTime().isBefore(task.getStartTime()) ||
                            newTask.getStartTime().isAfter(task.getEndTime()))) {
                return true;
            }
        }

        return false;
    }


    private void setEpicStartDataTime(int epicId) {
        Epic epic = epics.get(epicId);
        List<Integer> subtasksId = epic.getIdSubtasks();

        if (subtasksId.isEmpty()) {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(0);
        }

        LocalDateTime epicStartTime = null;
        LocalDateTime epicEndTime = null;
        int epicDuration = 0;

        for (Integer subtaskId : subtasksId) {
            Subtask subtask = subtasks.get(subtaskId);
            LocalDateTime subtaskStartTime = subtask.getStartTime();
            LocalDateTime subtaskEndTime = subtask.getEndTime();

            if (subtaskStartTime != null) {
                if (epicStartTime == null || subtaskStartTime.isBefore(epicStartTime)) {
                    epicStartTime = subtaskStartTime;
                }
            }

            if (subtaskEndTime != null) {
                if (epicEndTime == null || subtaskEndTime.isAfter(epicEndTime)) {
                    epicEndTime = subtaskEndTime;
                }
            }
            epicDuration += subtasks.get(subtaskId).getDuration();
        }

        epic.setStartTime(epicStartTime);
        epic.setEndTime(epicEndTime);
        epic.setDuration(epicDuration);
    }
}