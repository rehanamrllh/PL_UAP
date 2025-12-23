package com.uap;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// --- MODEL ---
class Task {
    private String id;
    private String title;
    private String description;
    private String priority; // High, Medium, Low
    private String status;   // Pending, Completed
    private LocalDate createdDate;
    private LocalDate dueDate;

    public Task(String id, String title, String description, String priority, String status, LocalDate createdDate, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public LocalDate getCreatedDate() { return createdDate; }
    public LocalDate getDueDate() { return dueDate; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setStatus(String status) { this.status = status; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    // Convert to CSV string
    public String toCSV() {
        return id + "," + title + "," + description + "," + priority + "," + status + "," + createdDate + "," + dueDate;
    }

    // Create from CSV string
    public static Task fromCSV(String line) {
        String[] parts = line.split(",");
        return new Task(parts[0], parts[1], parts[2], parts[3], parts[4], LocalDate.parse(parts[5]), LocalDate.parse(parts[6]));
    }
}

// --- CONTROLLER / DATA HANDLER ---
class DataManager {
    private static final String FILE_NAME = "tasks_data.csv";
    private List<Task> tasks;

    public DataManager() {
        tasks = new ArrayList<>();
        loadData();
    }

    public List<Task> getTasks() { return tasks; }

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
        if (!file.exists()) return;

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
    
    // Helper for stats
    public long countTasks() { return tasks.size(); }
    public long countCompleted() { return tasks.stream().filter(t -> t.getStatus().equals("Completed")).count(); }
    public long countPending() { return tasks.stream().filter(t -> t.getStatus().equals("Pending")).count(); }
}