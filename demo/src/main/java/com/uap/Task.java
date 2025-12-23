package com.uap;

import java.time.LocalDate;

public class Task {
    private final String id;
    private String title;
    private String description;
    private String priority; // High, Medium, Low
    private String status; // Pending, Completed
    private final LocalDate createdDate;
    private LocalDate dueDate;

    public Task(String id, String title, String description, String priority, String status, LocalDate createdDate,
            LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String toCSV() {
        return id + "," + title + "," + description + "," + priority + "," + status + "," + createdDate + "," + dueDate;
    }

    public static Task fromCSV(String line) {
        String[] parts = line.split(",");
        return new Task(parts[0], parts[1], parts[2], parts[3], parts[4], LocalDate.parse(parts[5]),
                LocalDate.parse(parts[6]));
    }
}
