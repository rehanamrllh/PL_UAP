package com.uap;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles file operations for data persistence.
 * Implements exception handling for robust file operations.
 */
public class FileHandler {
    private static final String DATA_FILE = "tasks.csv";
    private static final String HEADER = "ID,Title,Description,Priority,Status,CreatedDate,CompletedDate";

    /**
     * Save tasks to CSV file
     */
    public static void saveTasks(List<Task> tasks) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            // Write header
            writer.write(HEADER);
            writer.newLine();

            // Write each task
            for (Task task : tasks) {
                writer.write(task.toCSV());
                writer.newLine();
            }

            System.out.println("Tasks saved successfully to " + DATA_FILE);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
            throw new IOException("Failed to save tasks to file: " + e.getMessage(), e);
        }
    }

    /**
     * Load tasks from CSV file
     */
    public static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(DATA_FILE);

        // If file doesn't exist, return empty list
        if (!file.exists()) {
            System.out.println("No existing data file found. Starting with empty task list.");
            return tasks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Skip header
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Task task = parseTaskFromCSV(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineNumber + ": " + e.getMessage());
                    // Continue loading other tasks even if one fails
                }
            }

            System.out.println("Loaded " + tasks.size() + " tasks from " + DATA_FILE);
        } catch (FileNotFoundException e) {
            System.err.println("Data file not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading tasks file: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Parse a Task object from CSV line
     */
    private static Task parseTaskFromCSV(String csvLine) throws Exception {
        List<String> values = parseCsvLine(csvLine);

        if (values.size() < 7) {
            throw new Exception("Invalid CSV format: Expected 7 fields, got " + values.size());
        }

        try {
            int id = Integer.parseInt(values.get(0).trim());
            String title = values.get(1).trim();
            String description = values.get(2).trim();
            String priority = values.get(3).trim();
            String status = values.get(4).trim();
            String createdDate = values.get(5).trim();
            String completedDate = values.get(6).trim();

            // Validate required fields
            if (title.isEmpty()) {
                throw new Exception("Task title cannot be empty");
            }

            return new Task(id, title, description, priority, status, createdDate, completedDate);
        } catch (NumberFormatException e) {
            throw new Exception("Invalid ID format: " + e.getMessage());
        }
    }

    /**
     * Parse CSV line handling quoted fields
     */
    private static List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                // Handle double quotes (escaped quotes)
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    currentValue.append('"');
                    i++; // Skip next quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                values.add(currentValue.toString());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }

        // Add last value
        values.add(currentValue.toString());

        return values;
    }

    /**
     * Create backup of current data file
     */
    public static void createBackup() {
        File sourceFile = new File(DATA_FILE);
        if (!sourceFile.exists()) {
            return;
        }

        String backupFileName = "tasks_backup_" + System.currentTimeMillis() + ".csv";
        File backupFile = new File(backupFileName);

        try (InputStream in = new FileInputStream(sourceFile);
                OutputStream out = new FileOutputStream(backupFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            System.out.println("Backup created: " + backupFileName);
        } catch (IOException e) {
            System.err.println("Error creating backup: " + e.getMessage());
        }
    }

    /**
     * Check if data file exists
     */
    public static boolean dataFileExists() {
        return new File(DATA_FILE).exists();
    }

    /**
     * Get data file path
     */
    public static String getDataFilePath() {
        return new File(DATA_FILE).getAbsolutePath();
    }
}
