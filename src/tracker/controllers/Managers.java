package tracker.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tracker.http.adapter.LocalDateTimeAdapter;
import java.time.LocalDateTime;

public class Managers {
    private static Gson gson;

    public static TaskManager getTaskManager() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
        }
        return gson;
    }
}
