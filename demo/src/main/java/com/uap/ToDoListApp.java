package com.uap;

import javax.swing.*;

public class ToDoListApp {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set system look and feel: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("==============================================");
                System.out.println("   To-Do List Manager Application");
                System.out.println("   UAP Pemrograman Lanjut 2025");
                System.out.println("==============================================");
                System.out.println();

                MainDashboard dashboard = new MainDashboard();
                dashboard.setVisible(true);

                System.out.println("Application started successfully!");
                System.out.println("Data file location: " + FileHandler.getDataFilePath());
                System.out.println();

            } catch (Exception e) {
                System.err.println("Error starting application: " + e.getMessage());
                e.printStackTrace();

                JOptionPane.showMessageDialog(null,
                        "Failed to start application:\n" + e.getMessage(),
                        "Application Error",
                        JOptionPane.ERROR_MESSAGE);

                System.exit(1);
            }
        });
    }
}
