package com.uap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Model class representing a Task in the To-Do List application.
 * Implements Comparable for sorting functionality.
 */
public class Task implements Comparable<Task> {
    private int id;
    private String title;
    private String description;
    private String priority; // HIGH, MEDIUM, LOW
    private String status; // PENDING, IN_PROGRESS, COMPLETED
    private LocalDate createdDate;
    private LocalDate completedDate;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Constructor for new tasks
    public Task(int id, String title, String description, String priority, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.createdDate = LocalDate.now();
        this.completedDate = null;
    }

    // Constructor for loading tasks from file
    public Task(int id, String title, String description, String priority, String status,
            String createdDate, String completedDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;

        try {
            this.createdDate = LocalDate.parse(createdDate, DATE_FORMATTER);
            this.completedDate = (completedDate != null && !completedDate.isEmpty())
                    ? LocalDate.parse(completedDate, DATE_FORMATTER)
                    : null;
        } catch (Exception e) {
            this.createdDate = LocalDate.now();
            this.completedDate = null;
        }
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        if ("COMPLETED".equals(status) && completedDate == null) {
            this.completedDate = LocalDate.now();
        }
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDate completedDate) {
        this.completedDate = completedDate;
    }

    // Format dates for display
    public String getFormattedCreatedDate() {
        return createdDate != null ? createdDate.format(DATE_FORMATTER) : "";
    }

    public String getFormattedCompletedDate() {
        return completedDate != null ? completedDate.format(DATE_FORMATTER) : "";
    }

    // Convert to CSV format for file storage
    public String toCSV() {
        return String.format("%d,%s,%s,%s,%s,%s,%s",
                id,
                escapeCsv(title),
                escapeCsv(description),
                priority,
                status,
                getFormattedCreatedDate(),
                getFormattedCompletedDate());
    }

    // Escape special characters in CSV
    private String escapeCsv(String value) {
        if (value == null)
            return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    // Compare tasks for sorting (by priority and created date)
    @Override
    public int compareTo(Task other) {
        // First compare by priority
        int priorityCompare = getPriorityValue(this.priority) - getPriorityValue(other.priority);
        if (priorityCompare != 0) {
            return priorityCompare;
        }
        // Then by created date (newest first)
        return other.createdDate.compareTo(this.createdDate);
    }

    private int getPriorityValue(String priority) {
        switch (priority) {
            case "HIGH":
                return 1;
            case "MEDIUM":
                return 2;
            case "LOW":
                return 3;
            default:
                return 4;
        }
    }

    @Override
    public String toString() {
        return String.format("Task[id=%d, title=%s, priority=%s, status=%s]",
                id, title, priority, status);
    }
}
