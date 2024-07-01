package controllers;

import org.junit.jupiter.api.Test;
import tracker.controllers.HistoryManager;
import tracker.controllers.TaskManager;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tracker.controllers.Managers.getDefault;
import static tracker.controllers.Managers.getDefaultHistory;


class ManagersTest {

    @Test
    void testGetInitializedManagersDefault() {
        TaskManager taskManager = getDefault();
        assertNotNull(taskManager);
    }

    @Test
    void testGetInitializedHistoryManager() {
        HistoryManager historyManager = getDefaultHistory();
        assertNotNull(historyManager);

    }

}