package exception;

import org.junit.jupiter.api.Test;
import tracker.controllers.FileBackedTaskManager;
import tracker.exception.ManagerSaveException;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ManagerSaveExceptionTest {
    @Test
    void fileIsDirectoryTest() {
        File file = new File("resources/");

        assertThrows(ManagerSaveException.class, () -> FileBackedTaskManager.loadFromFile(file),
                "Ошибка проверки на содержание пути в переменной вместо файла");
    }
}
