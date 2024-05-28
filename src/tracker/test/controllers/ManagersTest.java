package tracker.test.controllers;

import org.junit.jupiter.api.Test;
import tracker.controllers.HistoryManager;
import tracker.controllers.Managers;
import tracker.controllers.TaskManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {

    @Test
    void testGetInitializedManagers() {
        TaskManager taskManager = new Managers().inMemoryTaskManager();
        assertNotNull(taskManager);
    }

    @Test
    void testGetInitializedManagersDefault() {
        TaskManager taskManager = new Managers().getDefault();
        assertNotNull(taskManager);
    }

    @Test
    void testGetInitializedHistoryManager() {
        HistoryManager historyManager = new Managers().getDefaultHistory();
        assertNotNull(historyManager);

    }

}