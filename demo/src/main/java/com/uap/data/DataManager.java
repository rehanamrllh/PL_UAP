package com.uap.data;

import com.uap.model.Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final String FILE_NAME = "tasks_data.csv";

    private final List<Task> tasks;

    public DataManager() {
        tasks = new ArrayList<>();
        loadData();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveData();
    }

    public void updateTask(String id, Task updatedTask) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(id)) {
                tasks.set(i, updatedTask);
                break;
            }
        }
        saveData();
    }

    public void deleteTask(String id) {
        tasks.removeIf(t -> t.getId().equals(id));
        saveData();
    }

    public String generateUniqueId() {
        int maxId = 0;
        for (Task task : tasks) {
            String existingId = task.getId();
            if (existingId == null) {
                continue;
            }

            try {
                int numericId = Integer.parseInt(existingId.trim());
                if (numericId > maxId) {
                    maxId = numericId;
                }
            } catch (NumberFormatException ignored) {
                // If older data contains non-numeric IDs, ignore them for sequencing.
            }
        }
        return String.valueOf(maxId + 1);
    }

    private void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Task task : tasks) {
                writer.write(task.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    tasks.add(Task.fromCSV(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long countTasks() {
        return tasks.size();
    }

    public long countCompleted() {
        return tasks.stream().filter(t -> t.getStatus().equals("Completed")).count();
    }

    public long countPending() {
        return tasks.stream().filter(t -> t.getStatus().equals("Pending")).count();
    }
}
