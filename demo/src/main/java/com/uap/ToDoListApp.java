package com.uap;

import javax.swing.*;

/**
 * Main entry point for the To-Do List Application.
 * This class launches the GUI application.
 * 
 * UAP Pemrograman Lanjut 2025
 * 
 * Features:
 * - Graphical User Interface using Java Swing
 * - CRUD Operations (Create, Read, Update, Delete)
 * - File Handling for data persistence (CSV format)
 * - Exception Handling for robust error management
 * - 4 Main Screens: Dashboard, Task List, Add/Edit Task, History
 * - Sorting and Searching functionality
 * - User-friendly and attractive design
 */
public class ToDoListApp {

    public static void main(String[] args) {
        // Set look and feel to system default for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set system look and feel: " + e.getMessage());
            // Continue with default look and feel
        }

        // Run the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("==============================================");
                System.out.println("   To-Do List Manager Application");
                System.out.println("   UAP Pemrograman Lanjut 2025");
                System.out.println("==============================================");
                System.out.println();

                // Create and display the main dashboard
                MainDashboard dashboard = new MainDashboard();
                dashboard.setVisible(true);

                System.out.println("Application started successfully!");
                System.out.println("Data file location: " + FileHandler.getDataFilePath());
                System.out.println();

            } catch (Exception e) {
                System.err.println("Error starting application: " + e.getMessage());
                e.printStackTrace();

                // Show error dialog
                JOptionPane.showMessageDialog(null,
                        "Failed to start application:\n" + e.getMessage(),
                        "Application Error",
                        JOptionPane.ERROR_MESSAGE);

                System.exit(1);
            }
        });
    }
}
