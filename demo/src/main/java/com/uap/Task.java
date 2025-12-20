package com.uap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task implements Comparable<Task> {
    private int id;
    private String title;
    private String description;
    private String priority; 
    private String status; 
    private LocalDate createdDate;
    private LocalDate completedDate;
    private LocalDate dueDate;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Task(int id, String title, String description, String priority, String status, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.createdDate = LocalDate.now();
        this.completedDate = null;
        this.dueDate = dueDate;
    }

    public Task(int id, String title, String description, String priority, String status,
            String createdDate, String completedDate, String dueDate) {
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
            this.dueDate = (dueDate != null && !dueDate.isEmpty())
                    ? LocalDate.parse(dueDate, DATE_FORMATTER)
                    : null;
        } catch (Exception e) {
            this.createdDate = LocalDate.now();
            this.completedDate = null;
            this.dueDate = null;
        }
    }

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
        if ("Complete".equals(status) && completedDate == null) {
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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getFormattedCreatedDate() {
        return createdDate != null ? createdDate.format(DATE_FORMATTER) : "";
    }

    public String getFormattedCompletedDate() {
        return completedDate != null ? completedDate.format(DATE_FORMATTER) : "";
    }

    public String getFormattedDueDate() {
        return dueDate != null ? dueDate.format(DATE_FORMATTER) : "";
    }

    public String toCSV() {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                id,
                escapeCsv(title),
                escapeCsv(description),
                priority,
                status,
                getFormattedCreatedDate(),
                getFormattedCompletedDate(),
                getFormattedDueDate());
    }

    private String escapeCsv(String value) {
        if (value == null)
            return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    @Override
    public int compareTo(Task other) {
        int priorityCompare = getPriorityValue(this.priority) - getPriorityValue(other.priority);
        if (priorityCompare != 0) {
            return priorityCompare;
        }
        return other.createdDate.compareTo(this.createdDate);
    }

    private int getPriorityValue(String priority) {
        switch (priority) {
            case "High":
                return 1;
            case "Medium":
                return 2;
            case "Low  ":
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
