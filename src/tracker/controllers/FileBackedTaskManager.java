package tracker.controllers;

import tracker.enums.Status;
import tracker.enums.TaskType;
import tracker.exception.ManagerSaveException;
import tracker.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File fileBackendTask;

    private final String firstLine = "id,type,name,status,description,startTime,endTime,duration,epic_id";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm");

    public FileBackedTaskManager(File fileBackendTask) {
        this.fileBackendTask = fileBackendTask;
    }

    public void save() {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileBackendTask,
                StandardCharsets.UTF_8))) {
            bufferedWriter.write(firstLine);
            bufferedWriter.newLine();

            for (Task task : getTasks()) {
                bufferedWriter.write(toString(task));
                bufferedWriter.newLine();
            }

            for (Epic epic : getEpics()) {
                bufferedWriter.write(toString(epic));
                bufferedWriter.newLine();
            }

            for (Subtask subtask : getSubtasks()) {
                bufferedWriter.write(toString(subtask));
                bufferedWriter.newLine();
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Oшибка с записью в файла." + e.getMessage());
        }
    }

    private String toString(Task task) {
        return task.getId() + "," + task.getTaskType() + ',' + task.getTitle() + ',' + task.getStatus() + ','
                + task.getDescription() + ',' + task.getStartTimeString() + ',' + task.getEndTimeString()
                + "," + task.getDuration();
    }

    private String toString(Epic epic) {
        return epic.getId() + "," + epic.getTaskType() + ',' + epic.getTitle() + ',' + epic.getStatus() + ','
                + epic.getDescription() + ',' + epic.getStartTimeString() + ',' + epic.getEndTimeEpicString()
                + "," + epic.getDuration();
    }

    private String toString(Subtask subtask) {
        return subtask.getId() + "," + subtask.getTaskType() + ',' + subtask.getTitle() + ',' + subtask.getStatus()
                + ',' + subtask.getDescription() + ',' + subtask.getStartTimeString() + ',' + subtask.getEndTimeString()
                + "," + subtask.getDuration() + ','  + subtask.getIdEpic();
    }

    private static Task fromString(String value) {
        // "id,type,name,status,description,startTime,endTime,duration,epic_id";
        String[] split = value.split(",");
        int taskId = Integer.parseInt(split[0]);
        TaskType taskType = TaskType.valueOf(split[1]);
        String taskTitle = split[2];
        Status taskStatus = Status.valueOf(split[3]);
        String taskDescription = split[4];
        LocalDateTime startTime;
        LocalDateTime endTime;
        Duration duration = Duration.ofMinutes(Integer.parseInt(split[7]));

        if (split[5].equals("null")) {
            startTime = null;
            endTime = null;
        } else {
            startTime = LocalDateTime.parse(split[5], FORMATTER);
            endTime = LocalDateTime.parse(split[5], FORMATTER);
        }


        switch (taskType) {
            case TASK -> {
                return new Task(taskId, taskTitle, taskDescription, taskStatus, startTime, duration);
            }
            case SUBTASK -> {
                int idEpicForSubtask = Integer.parseInt(split[8]);

                return new Subtask(taskId, taskTitle, taskDescription, taskStatus, startTime, duration, idEpicForSubtask);
            }
            case EPIC -> {
                return new Epic(taskId, taskTitle, taskDescription, taskStatus, startTime, duration, endTime);
            }

            default -> throw new IllegalStateException("Unexpected value: " + taskType);
        }
    }

    public static FileBackedTaskManager loadFromFile(File fileBackendTask) {
        FileBackedTaskManager manager = new FileBackedTaskManager(fileBackendTask);

        try (BufferedReader br = new BufferedReader(new FileReader(fileBackendTask,
                StandardCharsets.UTF_8))) {
            while (br.ready()) {
                String line = br.readLine();
                if (line.equals(manager.firstLine)) {
                    continue;
                }
                Task task = fromString(line);

                switch (task.getTaskType()) {
                    case TASK -> {
                        manager.createTask(task);
                        manager.prioritizedTasks.add(task);
                    }
                    case SUBTASK -> {
                        manager.createSubTask((Subtask) task);
                        manager.prioritizedTasks.add(task);
                    }
                    case EPIC -> manager.createEpic((Epic) task);
                    default -> throw new IllegalStateException("Unexpected value: " + task.getTaskType());
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при работе с файлом. " + e.getMessage());
        }
        return manager;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEic() {
        super.deleteAllEic();
        save();
    }

    @Override
    public void deleteTaskById(Integer requiredId) {
        super.deleteTaskById(requiredId);
        save();
    }

    @Override
    public void deleteEpicById(Integer requiredId) {
        super.deleteEpicById(requiredId);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer requiredId) {
        super.deleteSubtaskById(requiredId);
        save();
    }

    @Override
    public void createTask(Task newTask) {
        super.createTask(newTask);
        save();
    }

    @Override
    public void createEpic(Epic newEpic) {
        super.createEpic(newEpic);
        save();
    }

    @Override
    public void createSubTask(Subtask newSubtask) {
        super.createSubTask(newSubtask);
        save();
    }

    @Override
    public void updateEpicStatus(Integer epicId) {
        super.updateEpicStatus(epicId);
        save();
    }

    @Override
    public void updateSubTask(Subtask newSubtask) {
        super.updateSubTask(newSubtask);
        save();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateEpic(Epic newTask) {
        super.updateEpic(newTask);
        save();
    }
}