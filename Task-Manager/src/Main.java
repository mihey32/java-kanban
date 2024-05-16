public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        //Иницилизируем и создаем задачи:
        Task task1 = new Task("Задача 1", "Описание1");
        Task task2 = new Task("Задача 2", "Описание2");
        taskManager.createTask(task1);
        taskManager.createTask(task2);


        // Иницилизируем и создаем Эпики
        Epic epic1 = new Epic("Эпик 1", "Описание1");
        Epic epic2 = new Epic("Эпик 2", "Описание2");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        //Иницилизируем и создаем Подзадачи
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание1", 3);
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание2", 3);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        Task task3 = new Task(task1.getId(), "Обновленная задача 1", "Описание изменилось", Status.IN_PROGRESS);
        Epic epic3 = new Epic(epic1.getId(), "Обновленная задача 1", "Описание изменилось", Status.DONE);

        taskManager.getListTasks();

        System.out.println("---------");
        SubTask subTask3 = new SubTask(subTask1.getId(), "Обновленная подзадача",
                "Описание изменилось", Status.DONE, subTask1.getIdEpic());
        SubTask subTask4 = new SubTask(subTask2.getId(), "Обновленная подзадача",
                "Описание изменилось", Status.DONE, subTask2.getIdEpic());



        taskManager.deleteTaskById(2);
        taskManager.getListTasks();

        taskManager.getTaskById(1);
        taskManager.getTaskById(4);
        taskManager.getTaskById(18);
        taskManager.updateTask(task3);
        taskManager.updateEpic(epic3);  // проверить еще раз
        taskManager.getListTasks();


        taskManager.updateSubTask(subTask3);
        taskManager.updateSubTask(subTask4);


        taskManager.getListTasks();

       for (int i = 1; i < 10; i++) {
            taskManager.getTaskById(i);
        }

       taskManager.getSubtasksOfEpic(epic1);
        taskManager.getSubtasksOfEpic(epic2);
      taskManager.deleteTaskById(1);
        taskManager.deleteTaskById(2);
        taskManager.deleteTaskById(-2);
        taskManager.deleteTaskById(10);

        taskManager.getListTasks();
        taskManager.deleteAllTasks();










    }
}
