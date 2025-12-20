package com.uap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private List<Task> tasks;
    private int nextId;

    public TaskManager() {
        this.tasks = new ArrayList<>();
        loadData();
    }

    private void loadData() {
        try {
            tasks = FileHandler.loadTasks();

            nextId = tasks.stream()
                    .mapToInt(Task::getId)
                    .max()
                    .orElse(0) + 1;

            System.out.println("TaskManager initialized with " + tasks.size() + " tasks");
        } catch (Exception e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            tasks = new ArrayList<>();
            nextId = 1;
        }
    }

    public void saveData() throws IOException {
        try {
            FileHandler.saveTasks(tasks);
        } catch (IOException e) {
            System.err.println("Failed to save tasks: " + e.getMessage());
            throw e;
        }
    }

    public Task addTask(String title, String description, String priority, String status, java.time.LocalDate dueDate)
            throws IllegalArgumentException {
        // Validate input
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }

        if (priority == null || priority.trim().isEmpty()) {
            priority = "MEDIUM";
        }

        if (status == null || status.trim().isEmpty()) {
            status = "PENDING";
        }

        Task task = new Task(nextId++, title.trim(), description.trim(),
                priority.toUpperCase(), status.toUpperCase(), dueDate);
        tasks.add(task);

        try {
            saveData();
            System.out.println("Task added: " + task);
            return task;
        } catch (IOException e) {
            tasks.remove(task);
            nextId--;
            throw new RuntimeException("Failed to save task: " + e.getMessage(), e);
        }
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public Task getTaskById(int id) {
        return tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean updateTask(int id, String title, String description,
            String priority, String status, java.time.LocalDate dueDate) {
        Task task = getTaskById(id);
        if (task == null) {
            System.err.println("Task not found: " + id);
            return false;
        }

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }

        task.setTitle(title.trim());
        task.setDescription(description.trim());
        task.setPriority(priority.toUpperCase());
        task.setStatus(status.toUpperCase());
        task.setDueDate(dueDate);

        try {
            saveData();
            System.out.println("Task updated: " + task);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to save updated task: " + e.getMessage());
            throw new RuntimeException("Failed to save task: " + e.getMessage(), e);
        }
    }

    public boolean deleteTask(int id) {
        Task task = getTaskById(id);
        if (task == null) {
            System.err.println("Task not found: " + id);
            return false;
        }

        tasks.remove(task);

        try {
            saveData();
            System.out.println("Task deleted: " + task);
            return true;
        } catch (IOException e) {
            tasks.add(task);
            System.err.println("Failed to delete task: " + e.getMessage());
            throw new RuntimeException("Failed to delete task: " + e.getMessage(), e);
        }
    }

    public List<Task> searchTasks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllTasks();
        }

        String searchTerm = keyword.toLowerCase().trim();
        return tasks.stream()
                .filter(t -> t.getTitle().toLowerCase().contains(searchTerm) ||
                        t.getDescription().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByStatus(String status) {
        if (status == null || status.trim().isEmpty() || "ALL".equalsIgnoreCase(status)) {
            return getAllTasks();
        }

        return tasks.stream()
                .filter(t -> t.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    public List<Task> getTasksByPriority(String priority) {
        if (priority == null || priority.trim().isEmpty() || "ALL".equalsIgnoreCase(priority)) {
            return getAllTasks();
        }

        return tasks.stream()
                .filter(t -> t.getPriority().equalsIgnoreCase(priority))
                .collect(Collectors.toList());
    }

    public List<Task> getCompletedTasks() {
        return tasks.stream()
                .filter(t -> "COMPLETED".equals(t.getStatus()))
                .sorted(Comparator.comparing(Task::getCompletedDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    public List<Task> getPendingTasks() {
        return tasks.stream()
                .filter(t -> !"COMPLETED".equals(t.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Task> sortTasks(List<Task> taskList, String sortBy, boolean ascending) {
        List<Task> sortedList = new ArrayList<>(taskList);

        Comparator<Task> comparator;
        switch (sortBy.toUpperCase()) {
            case "TITLE":
                comparator = Comparator.comparing(Task::getTitle);
                break;
            case "PRIORITY":
                comparator = Comparator.comparingInt(t -> getPriorityValue(t.getPriority()));
                break;
            case "STATUS":
                comparator = Comparator.comparing(Task::getStatus);
                break;
            case "CREATED":
                comparator = Comparator.comparing(Task::getCreatedDate);
                break;
            case "COMPLETED":
                comparator = Comparator.comparing(Task::getCompletedDate,
                        Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            default:
                comparator = Comparator.comparingInt(Task::getId);
        }

        if (!ascending) {
            comparator = comparator.reversed();
        }

        sortedList.sort(comparator);
        return sortedList;
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

    public TaskStatistics getStatistics() {
        long total = tasks.size();
        long completed = tasks.stream().filter(t -> "COMPLETED".equals(t.getStatus())).count();
        long pending = tasks.stream().filter(t -> "PENDING".equals(t.getStatus())).count();
        long inProgress = tasks.stream().filter(t -> "IN_PROGRESS".equals(t.getStatus())).count();

        long highPriority = tasks.stream().filter(t -> "HIGH".equals(t.getPriority())).count();
        long mediumPriority = tasks.stream().filter(t -> "MEDIUM".equals(t.getPriority())).count();
        long lowPriority = tasks.stream().filter(t -> "LOW".equals(t.getPriority())).count();
        return new TaskStatistics(total, completed, pending, inProgress,
                highPriority, mediumPriority, lowPriority);
    }

    public static class TaskStatistics {
        public final long total;
        public final long completed;
        public final long pending;
        public final long inProgress;
        public final long highPriority;
        public final long mediumPriority;
        public final long lowPriority;

        public TaskStatistics(long total, long completed, long pending, long inProgress,
                long highPriority, long mediumPriority, long lowPriority) {
            this.total = total;
            this.completed = completed;
            this.pending = pending;
            this.inProgress = inProgress;
            this.highPriority = highPriority;
            this.mediumPriority = mediumPriority;
            this.lowPriority = lowPriority;
        }

        public double getCompletionRate() {
            return total > 0 ? (completed * 100.0 / total) : 0.0;
        }
    }
}
